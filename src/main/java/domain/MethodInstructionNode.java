package domain;

public interface MethodInstructionNode extends InstructionNode {
    public String getMethodName();

    public String getMethodOwner();

    public String getMethodDesc();
}
