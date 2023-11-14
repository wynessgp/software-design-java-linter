package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class FieldNodeASM implements FieldNode {
    private org.objectweb.asm.tree.FieldNode fieldNode;

    public FieldNodeASM(org.objectweb.asm.tree.FieldNode fieldNode) {
        this.fieldNode = fieldNode;
    }

    @Override
    public boolean matchesAccess(String access) {
        for (String a : access.split(" ")) {
            int accessLevel = 0;
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
            // check based on each individual string. If any of them are 0
            // AKA doesn't match, then we don't match overall.
            if ((accessLevel & fieldNode.access) == 0)
                return false; 
        }
        // we've matched for all of the strings, so we're good.
        return true;
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
