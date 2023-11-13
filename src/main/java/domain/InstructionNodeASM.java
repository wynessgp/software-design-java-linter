package domain;

import org.objectweb.asm.tree.AbstractInsnNode;

public class InstructionNodeASM implements InstructionNode {
    protected AbstractInsnNode instructionNode;

    public InstructionNodeASM(AbstractInsnNode instructionNode) {
        this.instructionNode = instructionNode;
    }

    // TODO: better way later. This is pretty bad lol
    @Override
    public boolean matchesInstructionType(String type) {
        int calcType = 0;
        switch (type.toLowerCase()) {
            case "field_insn":
                calcType = AbstractInsnNode.FIELD_INSN;
                break;
            case "method_insn":
                calcType = AbstractInsnNode.METHOD_INSN;
                break;
            case "var_insn":
                calcType = AbstractInsnNode.VAR_INSN;
                break;
            // add more as we need
        }
        return calcType == instructionNode.getType();
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
