package domain;

import org.objectweb.asm.tree.FieldInsnNode;

public class FieldInstructionNodeASM extends InstructionNodeASM implements FieldInstructionNode {
    private FieldInsnNode fieldInstructionNode;

    public FieldInstructionNodeASM(InstructionNode instructionNode) {
        super(((InstructionNodeASM) instructionNode).instructionNode);
        this.fieldInstructionNode =
                (FieldInsnNode) (((InstructionNodeASM) instructionNode).instructionNode);
    }

    public FieldInstructionNodeASM(FieldInsnNode instructionNode) {
        super(instructionNode);
        this.fieldInstructionNode = instructionNode;
    }

    // TODO: Enum?
    @Override
    public boolean matchesInstructionType(String type) {
        return type.toLowerCase().equals("field_insn");
    }

    @Override
    public String getFieldName() {
        return this.fieldInstructionNode.name.replace("/", ".");
    }

    @Override
    public String getFieldOwner() {
        return this.fieldInstructionNode.owner.replace("/", ".");
    }

    @Override
    public String getFieldDesc() {
        return this.fieldInstructionNode.desc.replace("/", ".");
    }

}
