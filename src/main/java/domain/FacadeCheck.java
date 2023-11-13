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

    private void trackDependency(String dependent, String dependency) {
        dependent = dependent.replace("/", ".");
        dependency = dependency.replace("/", ".");
        if (dependent.equals(dependency)) {
            return;
        }
        if (classDependencies.containsKey(dependent)) {
            classDependencies.get(dependent).add(dependency);
        } else {
            classDependencies.put(dependent, new HashSet<String>());
            classDependencies.get(dependent).add(dependency);
        }
    }

    @Override
    public List<String> handleResults() {
        List<String> facadeClasses = new ArrayList<>(visitedClasses);
        for (String s : classDependencies.keySet()) {
            for (String s2 : classDependencies.get(s)) {
                if (facadeClasses.contains(s2)) {
                    facadeClasses.remove(s2);
                }
            }
        }
        return facadeClasses.isEmpty() ? List.of("No facade classes detected")
                : facadeClasses.stream().map(s -> String.format("%s is a facade class", s))
                        .toList();
    }

    @Override
    public String getCheckName() {
        return "Facade";
    }
}
