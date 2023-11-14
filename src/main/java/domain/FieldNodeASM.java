package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class FieldNodeASM implements FieldNode {
    private org.objectweb.asm.tree.FieldNode fieldNode;

    public FieldNodeASM(org.objectweb.asm.tree.FieldNode fieldNode) {
        this.fieldNode = fieldNode;
    }

    /**
     * Checks for the access level of the field. Takes in a string of space-separated modifiers and
     * returns true if they match the integer access level.
     */
    @Override
    public boolean matchesAccess(String access) {
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
        return accessLevel == fieldNode.access;
    }

    @Override
    public String getDesc() {
        return this.fieldNode.desc.replace("/", ".");
    }

    @Override
    public String getFieldName() {
        return this.fieldNode.name.replace("/", ".");
    }

    @Override
    public Object getFieldValue() {
        return this.fieldNode.value;
    }

    @Override
    public String getFieldType() {
        return Type.getObjectType(this.fieldNode.desc).getClassName();
    }
}
