package domain;

import org.objectweb.asm.Opcodes;

public class FieldNodeASM implements FieldNode {
    private org.objectweb.asm.tree.FieldNode fieldNode;

    public FieldNodeASM(org.objectweb.asm.tree.FieldNode fieldNode) {
        this.fieldNode = fieldNode;
    }

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
            }
        }
        return accessLevel == fieldNode.access;
    }

    @Override
    public String getDesc() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDesc'");
    }

    @Override
    public String getFieldName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFieldName'");
    }

    @Override
    public Object getFieldValue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFieldValue'");
    }

    @Override
    public String getFieldType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFieldType'");
    }

}
