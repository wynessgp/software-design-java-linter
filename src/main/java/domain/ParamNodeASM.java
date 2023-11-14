package domain;

import org.objectweb.asm.Opcodes;

public class ParamNodeASM {
    /**
     * Checks for the access level of the parameter. Takes in a string of space-separated modifiers
     * and returns true if they match the integer access level.
     */
    public boolean matchesAccess(String access, int nodeAccessLevel) {
        int accessLevel = 0;
        for (String a : access.split(" ")) {
            switch (a.toLowerCase()) {
                case "public":
                    accessLevel += Opcodes.ACC_PUBLIC;
                    break;
                case "private":
                    accessLevel += Opcodes.ACC_PRIVATE;
                    break;
                case "protected":
                    accessLevel += Opcodes.ACC_PROTECTED;
                    break;
                case "static":
                    accessLevel += Opcodes.ACC_STATIC;
                    break;
                case "final":
                    accessLevel += Opcodes.ACC_FINAL;
                    break;
                case "abstract":
                    accessLevel += Opcodes.ACC_ABSTRACT;
                    break;
                case "interface":
                    accessLevel += Opcodes.ACC_INTERFACE;
                    break;
            }
        }
        return accessLevel == nodeAccessLevel;
    }

    public String getDesc(String unformattedDesc) {
        return unformattedDesc.replace("/", ".");
    }

    public String getName(String unformattedName) {
        return unformattedName.replace("/", ".");
    }
}
