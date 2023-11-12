package domain;

import org.objectweb.asm.tree.AbstractInsnNode;

// TODO: See if needed towards end of the project
public class InstructionNodeASM implements InstructionNode {
    private AbstractInsnNode instructionNode;

    public InstructionNodeASM(AbstractInsnNode instructionNode) {
        this.instructionNode = instructionNode;
    }

    // TODO: Make type an Enum for stuff like this
    @Override
    public boolean matchesInstructionType(String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'matchesInstructionType'");
    }

    @Override
    public int getOpcode() {
        return this.instructionNode.getOpcode();
    }

    @Override
    public InstructionNode getNextInstruction() {
        return this.instructionNode.getNext() == null ? null 
                : new InstructionNodeASM(this.instructionNode.getNext());
    }

    @Override
    public InstructionNode getPreviousInstruction() {
        return this.instructionNode.getPrevious() == null ? null 
                : new InstructionNodeASM(this.instructionNode.getPrevious());
    }

}
