package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FacadeCheck implements CheckStrategy {
    private Set<String> visitedClasses;
    private Map<String, Set<String>> classDependencies;

    public FacadeCheck() {
        this.visitedClasses = new HashSet<>();
        this.classDependencies = new HashMap<>();
    }

    @Override
    public void performCheck(List<ClassNode> classNames) {
        for (ClassNode classNode : classNames) {
            visitedClasses.add(classNode.getClassName().replace("/", "."));
            trackDependency(classNode.getClassName(), classNode.getSuperName());
            for (String interfaceName : classNode.getInterfaces()) {
                trackDependency(classNode.getClassName(), interfaceName);
            }
            parseFields(classNode);
            parseMethods(classNode);
        }
    }

    private void parseFields(ClassNode classNode) {
        for (FieldNode field : classNode.getFields()) {
            trackDependency(classNode.getClassName(), field.getFieldType());
        }
    }

    private void parseMethods(ClassNode classNode) {
        for (MethodNode method : classNode.getMethods()) {
            trackDependency(classNode.getClassName(), method.getReturnType());
            for (String argument : method.getArgTypes()) {
                trackDependency(classNode.getClassName(), argument);
            }
            parseInstructions(classNode, method);
        }
    }

    private void parseInstructions(ClassNode classNode, MethodNode method) {
        for (InstructionNode instruction : method.getInstructions()) {
            if (instruction.matchesInstructionType("method_insn")) {
                MethodInstructionNode methodInstruction = instruction.toMethodInstruction();
                trackDependency(classNode.getClassName(), methodInstruction.getMethodOwner());
            } else if (instruction.matchesInstructionType("field_insn")) {
                FieldInstructionNode fieldInstruction = instruction.toFieldInstruction();
                trackDependency(classNode.getClassName(), fieldInstruction.getFieldOwner());
            }
        }
    }

    /**
     * Adds a dependency to the class dependencies map. Ignores circular dependencies. If the
     * dependent and dependency packages don't match, do not add the dependency.
     */
    private void trackDependency(String dependent, String dependency) {
        dependent = dependent.replace("/", ".");
        dependency = dependency.replace("/", ".");
        if (dependent.equals(dependency)) {
            return;
        } else if (dependent.contains(".") && dependency.contains(".")) {
            String dependentPackage = dependent.substring(0, dependent.indexOf("."));
            String dependencyPackage = dependency.substring(0, dependency.indexOf("."));
            if (!dependentPackage.equals(dependencyPackage)) {
                return;
            } else if (classDependencies.containsKey(dependent)) {
                classDependencies.get(dependent).add(dependency);
            } else {
                classDependencies.put(dependent, new HashSet<String>());
                classDependencies.get(dependent).add(dependency);
            }
        }
    }

    @Override
    public List<String> handleResults() {
        List<String> potentialFacadeClasses = new ArrayList<>(visitedClasses);
        for (String s : classDependencies.keySet()) {
            for (String s2 : classDependencies.get(s)) {
                potentialFacadeClasses.remove(s2);
            }
        }
        Map<String, Set<String>> packageMap = new HashMap<>();
        for (String s : potentialFacadeClasses) {
            int lastDot = s.lastIndexOf(".");
            String[] packageAndClass =
                    new String[] {s.substring(0, lastDot), s.substring(lastDot + 1)};
            packageMap.putIfAbsent(packageAndClass[0], new HashSet<>());
            packageMap.get(packageAndClass[0]).add(packageAndClass[1]);
        }
        for (String pkg : packageMap.keySet()) {
            if (packageMap.get(pkg).size() > 1) {
                for (String className : packageMap.get(pkg)) {
                    potentialFacadeClasses.remove(String.format("%s.%s", pkg, className));
                }
            }
        }
        return potentialFacadeClasses.isEmpty() ? List.of("No facade classes detected")
                : potentialFacadeClasses.stream()
                        .map(s -> String.format("%s is possibly a facade class", s)).toList();
    }

    @Override
    public String getCheckName() {
        return "Facade";
    }
}
