package domain;

import java.util.Map;

import org.objectweb.asm.Opcodes;

public class MethodNodeASM implements MethodNode {
    private org.objectweb.asm.tree.MethodNode methodNode;

    public MethodNodeASM(org.objectweb.asm.tree.MethodNode methodNode) {
        this.methodNode = methodNode;
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
        return accessLevel == methodNode.access;
    }

    @Override
    public String getDesc() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDesc'");
    }

    @Override
    public String getMethodName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMethodName'");
    }

    @Override
    public InstructionNode getInstructions() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInstructions'");
    }

    @Override
    public String getReturnType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReturnType'");
    }

    @Override
    public Map<String, String> getArgs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArgs'");
    }

}
