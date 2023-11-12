package domain;

public interface InstructionNode {
    public boolean matchesInstructionType(String type);
    public int getOpcode();
    // TODO: Needed?
    public InstructionNode getNextInstruction();
    public InstructionNode getPreviousInstruction();
}
