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
            singleClassCheck(cn);
        }
    }

    @Override
    public List<String> handleResults() {
        List<String> ret = new ArrayList<>();
        for (String s : analyzedClasses) {
            if (classToViolatingCalls.containsKey(s)) {
                ret.add("\nClass: " + s
                        + " violates the Hollywood Principle by calling the following: \n");
                ret.add("\t\t" + classToViolatingCalls.get(s) + "\n");
            }
        }
        return ret;
    }

    @Override
    public String getCheckName() {
        return "Hollywood";
    }

    private void singleClassCheck(ClassNode cn) {
        String superName = cn.getSuperName();
        String className = cn.getClassName();
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

                // check to see if the method's name is one of the exceptions,
                // those being equals, hashCode, <init>, <clinit>

            }
        }
    }
}
