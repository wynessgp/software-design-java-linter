package domain;

import java.util.Map;

public class MethodNodeASM implements MethodNode {
    private org.objectweb.asm.tree.MethodNode methodNode;

    public MethodNodeASM(org.objectweb.asm.tree.MethodNode methodNode) {
        this.methodNode = methodNode;
    }

    @Override
    public String getAccess() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccess'");
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
