package domain;

import org.objectweb.asm.tree.VarInsnNode;

public class VarInstructionNodeASM implements VarInstructionNode {
    private VarInsnNode instructionNode;

    public VarInstructionNodeASM(VarInsnNode instructionNode) {
        this.instructionNode = instructionNode;
    }

    @Override
    public boolean matchesInstructionType(String type) {
        return type.toLowerCase().equals("var_insn");
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

    @Override
    public int getVarIndex() {
        return this.instructionNode.var;
    }
}
