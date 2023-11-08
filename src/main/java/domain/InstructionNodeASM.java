package domain;

import org.objectweb.asm.tree.AbstractInsnNode;

public class InstructionNodeASM implements InstructionNode {
    private AbstractInsnNode instructionNode;

    public InstructionNodeASM(AbstractInsnNode instructionNode) {
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

}
