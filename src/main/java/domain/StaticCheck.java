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
    private Map<String, Set<String>> declarations;
    private Map<String, Set<String>> classDependencies;

    public StaticCheck() {
        this.visitedClasses = new HashSet<>();
        this.nonStaticClasses = new HashSet<>();
        this.declarations = new HashMap<>();
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
            if (!field.matchesAccess("public static")) {
                nonStaticClasses.add(classNode.getClassName().replace("/", "."));
            }
            trackDependency(classNode.getClassName(), field.getFieldType());
            addDeclaredVariable(classNode.getClassName(), field.getFieldType());
        }
    }

    private void parseMethods(ClassNode classNode) {
        for (MethodNode method : classNode.getMethods()) {
            if (!method.matchesAccess("public static")
                    && !method.getMethodName().contains("init>")) {
                nonStaticClasses.add(classNode.getClassName().replace("/", "."));
            }
            trackDependency(classNode.getClassName(), method.getReturnType());
            for (String arg : method.getArgTypes()) {
                trackDependency(classNode.getClassName(), arg);
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

    private void addDeclaredVariable(String dependent, String dependency) {
        dependent = dependent.replace("/", ".");
        dependency = dependency.replace("/", ".");
        if (dependent.equals(dependency)) {
            return;
        }
        if (declarations.containsKey(dependent)) {
            declarations.get(dependent).add(dependency);
        } else {
            declarations.put(dependent, new HashSet<String>());
            declarations.get(dependent).add(dependency);
        }
    }

    @Override
    public List<String> handleResults() {
        List<String> staticViolations = new ArrayList<>();
        HashSet<String> staticClasses = new HashSet<>(visitedClasses);
        staticClasses.removeAll(nonStaticClasses);
        for (String from : declarations.keySet()) {
            for (String to : declarations.get(from)) {
                if (staticClasses.contains(to) && !from.equals(to)) {
                    staticViolations.add(String.format("Class %s should not declare %s", from, to));
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
