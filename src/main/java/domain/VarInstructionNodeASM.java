package domain;

import org.objectweb.asm.tree.VarInsnNode;

public class VarInstructionNodeASM extends InstructionNodeASM implements VarInstructionNode {
    private VarInsnNode varInstructionNode;

    public VarInstructionNodeASM(InstructionNode insnNode) {
        super(((InstructionNodeASM) insnNode).instructionNode);
        this.varInstructionNode = (VarInsnNode) (((InstructionNodeASM) insnNode).instructionNode);
    }

    public VarInstructionNodeASM(VarInsnNode instructionNode) {
        super(instructionNode);
        this.varInstructionNode = instructionNode;
    }

    @Override
    public boolean matchesInstructionType(String type) {
        return type.toLowerCase().equals("var_insn");
    }

    @Override
    public int getVarIndex() {
        return this.varInstructionNode.var;
    }
}
