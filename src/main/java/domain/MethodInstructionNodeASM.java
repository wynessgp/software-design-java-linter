package domain;

import org.objectweb.asm.tree.MethodInsnNode;

public class MethodInstructionNodeASM extends InstructionNodeASM implements MethodInstructionNode {
    private MethodInsnNode methodInstructionNode;

    public MethodInstructionNodeASM(InstructionNode insnNode) {
        super(((InstructionNodeASM) insnNode).instructionNode);
        this.methodInstructionNode = (MethodInsnNode) (((InstructionNodeASM) insnNode).instructionNode);
    }

    public MethodInstructionNodeASM(MethodInsnNode instructionNode) {
        super(instructionNode);
        this.methodInstructionNode = instructionNode;
    }

    @Override
    public boolean matchesInstructionType(String type) {
        return type.toLowerCase().equals("method_insn");
    }

    @Override
    public String getMethodName() {
        return this.methodInstructionNode.name.replace("/", ".");
    }

    @Override
    public String getMethodOwner() {
        return this.methodInstructionNode.owner.replace("/", ".");
    }

    @Override
    public String getMethodDesc() {
        return this.methodInstructionNode.desc.replace("/", ".");
    }
}
