package domain;

import org.objectweb.asm.tree.FieldInsnNode;

public class FieldInstructionNodeASM implements FieldInstructionNode {
    private FieldInsnNode instructionNode;

    public FieldInstructionNodeASM(FieldInsnNode instructionNode) {
        this.instructionNode = instructionNode;
    }

    @Override
    public boolean matchesInstructionType(String type) {
        return type.toLowerCase().equals("field");
    }

    @Override
    public int getOpcode() {
        return this.instructionNode.getOpcode();
    }

    // TODO: Needed?
    @Override
    public InstructionNode getNextInstruction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNextInstruction'");
    }

    // TODO: Needed?
    @Override
    public InstructionNode getPreviousInstruction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPreviousInstruction'");
    }

    @Override
    public String getFieldName() {
        // TODO Auto-generated method stub
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
