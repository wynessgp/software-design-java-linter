package domain;

public interface InstructionNode {
    public boolean matchesInstructionType(String type);

    public int getOpcode();

    public InstructionNode getNextInstruction();

    public InstructionNode getPreviousInstruction();
}
