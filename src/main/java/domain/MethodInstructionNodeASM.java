package domain;

import org.objectweb.asm.tree.MethodInsnNode;

public class MethodInstructionNodeASM implements MethodInstructionNode {
    private MethodInsnNode instructionNode;

    public MethodInstructionNodeASM(MethodInsnNode instructionNode) {
        this.instructionNode = instructionNode;
    }

    @Override
    public boolean matchesInstructionType(String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'matchesInstructionType'");
    }

    @Override
    public int getOpcode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOpcode'");
    }

    @Override
    public InstructionNode getNextInstruction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNextInstruction'");
    }

    @Override
    public InstructionNode getPreviousInstruction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPreviousInstruction'");
    }

    @Override
    public String getMethodName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMethodName'");
    }

    @Override
    public String getMethodOwner() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMethodOwner'");
    }

    @Override
    public String getMethodDesc() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMethodDesc'");
    }
    
}
