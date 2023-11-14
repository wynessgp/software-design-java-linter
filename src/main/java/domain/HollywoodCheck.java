package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HollywoodCheck implements CheckStrategy {
    private Map<String, Set<String>> classToViolatingCalls;
    private Set<String> analyzedClasses;

    public HollywoodCheck() {
        this.classToViolatingCalls = new HashMap<>();
        this.analyzedClasses = new HashSet<>();
    }

    @Override
    public void performCheck(List<ClassNode> classNames) {
        for (ClassNode cn : classNames) {
            parseInstructionsForViolation(cn);
        }
    }

    @Override
    public List<String> handleResults() {
        List<String> ret = new ArrayList<>();
        for (String s : analyzedClasses) {
            if (classToViolatingCalls.containsKey(s)) {
                ret.add("Class: " + s
                        + " violates the Hollywood Principle by calling the following: ");
                ret.add("\t" + classToViolatingCalls.get(s));
            } 
        }
        if (ret.isEmpty()) {
            ret.add("No violations detected");
        }
        return ret;
    }

    @Override
    public String getCheckName() {
        return "Hollywood";
    }

    // this isn't split up into multiple parts for methods, instructions, etc.
    // because the work would simply be looping in each one.
    private void parseInstructionsForViolation(ClassNode cn) {
        String superName = cn.getSuperName().replace("/", ".");
        String className = cn.getClassName().replace("/", ".");
        this.analyzedClasses.add(className);

        for (MethodNode mn : cn.getMethods()) {
            // instruction related code
            for (InstructionNode in : mn.getInstructions()) {
                // we're only interested if it's a method instruction.
                // There can't be violations outside of method instructions.
                if (!in.matchesInstructionType("method_insn"))
                    continue;
                // ok, so it's a method instruction.
                // do the casting.
                MethodInstructionNode min = in.toMethodInstruction();
                // check to see if the method's name is one of the exceptions,
                // those being equals, hashCode, <init>, <clinit>
                if (min.getMethodName().equals("equals") ||
                        min.getMethodName().equals("hashCode") ||
                        min.getMethodName().contains("init>"))
                    continue;
                // it's not one of our exceptions.
                // now, does the method belong to our superclass?
                if (min.getMethodOwner().replace("/", ".").equals(superName)) {
                    addViolation(min.getMethodName(), className);
                }
            }
        }
    }

    private void addViolation(String methodCallName, String classViolating) {
        if (classToViolatingCalls.containsKey(classViolating)) {
            classToViolatingCalls.get(classViolating).add("super." + methodCallName);
        } else {
            classToViolatingCalls.put(classViolating, new HashSet<String>());
            classToViolatingCalls.get(classViolating).add("super." + methodCallName);
        }
    }
}
