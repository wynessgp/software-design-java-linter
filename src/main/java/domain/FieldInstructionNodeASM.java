package domain;

import org.objectweb.asm.tree.FieldInsnNode;

public class FieldInstructionNodeASM implements FieldInstructionNode {
    private FieldInsnNode instructionNode;

    public FieldInstructionNodeASM(InstructionNode instructionNode) {
        this.instructionNode =
                (FieldInsnNode) (((InstructionNodeASM) instructionNode).instructionNode);
    }

    // TODO: Enum?
    @Override
    public boolean matchesInstructionType(String type) {
        return type.toLowerCase().equals("field_insn");
    }

    @Override
    public int getOpcode() {
        return this.instructionNode.getOpcode();
    }

    // TODO: Needed?
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
    public String getFieldName() {
        return this.instructionNode.name.replace("/", ".");
    }

    @Override
    public String getFieldOwner() {
        return this.instructionNode.owner.replace("/", ".");
    }

    @Override
    public String getFieldDesc() {
        return this.instructionNode.desc.replace("/", ".");
    }

}
