package domain;

import org.objectweb.asm.tree.FieldInsnNode;

public class FieldInstructionNodeASM implements FieldInstructionNode {
    private FieldInsnNode instructionNode;

    public FieldInstructionNodeASM(FieldInsnNode instructionNode) {
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
    public String getFieldName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFieldName'");
    }

    @Override
    public String getFieldOwner() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFieldOwner'");
    }

    @Override
    public String getFieldDesc() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFieldDesc'");
    }
    
}
