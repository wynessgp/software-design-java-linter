package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StaticCheck implements CheckStrategy {
    private Set<String> visitedClasses;
    private Set<String> nonStaticClasses;
    private Map<String, Set<String>> declared;

    public StaticCheck() {
        this.visitedClasses = new HashSet<>();
        this.nonStaticClasses = new HashSet<>();
        this.declared = new HashMap<>();
    }

    @Override
    public void performCheck(List<ClassNode> classNames) {
        for (ClassNode classNode : classNames) {
            visitedClasses.add(classNode.getClassName().replace("/", "."));
            parseFields(classNode);
            parseMethods(classNode);
        }
    }

    private void parseFields(ClassNode classNode) {
        for (FieldNode field : classNode.getFields()) {
            if (field.matchesAccess("public")) {
                nonStaticClasses.add(classNode.getClassName().replace("/", "."));
            }
            addDeclaredVariable(classNode.getClassName(), field.getFieldType());
        }
    }

    private void parseMethods(ClassNode classNode) {
        for (MethodNode method : classNode.getMethods()) {
            if (method.matchesAccess("public")
                    && !method.getMethodName().contains("init>")) {
                nonStaticClasses.add(classNode.getClassName().replace("/", "."));
            }
            parseInstructions(classNode, method);
        }
    }

    private void parseInstructions(ClassNode classNode, MethodNode method) {
        for (InstructionNode instruction : method.getInstructions()) {
            if (instruction.matchesInstructionType("method_insn")) {
                MethodInstructionNode methodInstruction = instruction.toMethodInstruction();
                if (methodInstruction.getMethodName().equals("<init>")) {
                    addDeclaredVariable(classNode.getClassName(), methodInstruction.getMethodOwner());
                }
            }
        }
    }
    
    private void addDeclaredVariable(String dependent, String dependency) {
        dependent = dependent.replace("/", ".");
        dependency = dependency.replace("/", ".");
        if (dependent.equals(dependency)) {
            return;
        }
        if (declared.containsKey(dependent)) {
            declared.get(dependent).add(dependency);
        } else {
            declared.put(dependent, new HashSet<String>());
            declared.get(dependent).add(dependency);
        }
    }

    @Override
    public List<String> handleResults() {
        List<String> staticViolations = new ArrayList<>();
        HashSet<String> staticClasses = new HashSet<>(visitedClasses);
        staticClasses.removeAll(nonStaticClasses);
        for (String from : declared.keySet()) {
			for (String to : declared.get(from)) {
				if (staticClasses.contains(to) && !from.equals(to)) {
                    staticViolations.add(String.format("Class %s should not declare %s", from,
                            to));
				}
			}
		}
        return staticViolations;
    }

    @Override
    public String getCheckName() {
        return "Static";
    }
}
