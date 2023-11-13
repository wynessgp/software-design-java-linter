package domain;

import org.objectweb.asm.tree.MethodInsnNode;

public class MethodInstructionNodeASM implements MethodInstructionNode {
    private MethodInsnNode instructionNode;

    public MethodInstructionNodeASM(InstructionNode instructionNode) {
        this.instructionNode =
                (MethodInsnNode) (((InstructionNodeASM) instructionNode).instructionNode);
    }

    // TODO: Enum?
    @Override
    public boolean matchesInstructionType(String type) {
        return type.toLowerCase().equals("method_insn");
    }

    @Override
    public int getOpcode() {
        return this.instructionNode.getOpcode();
    }

    // TODO: Evalutate if we need these near end of implementation
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

    @Override
    public String getMethodName() {
        return this.instructionNode.name.replace("/", ".");
    }

    @Override
    public String getMethodOwner() {
        return this.instructionNode.owner.replace("/", ".");
    }

    @Override
    public String getMethodDesc() {
        return this.instructionNode.desc.replace("/", ".");
    }

}
