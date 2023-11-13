package domain;

public interface FieldInstructionNode extends InstructionNode {
    public String getFieldName();

    public String getFieldOwner();

    public String getFieldDesc();
}
