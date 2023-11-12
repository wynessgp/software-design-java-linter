package domain;

import org.objectweb.asm.tree.MethodInsnNode;

public class MethodInstructionNodeASM implements MethodInstructionNode {
    private MethodInsnNode instructionNode;

    public MethodInstructionNodeASM(MethodInsnNode instructionNode) {
        this.instructionNode = instructionNode;
    }

    @Override
    public boolean matchesInstructionType(String type) {
        return type.toLowerCase().equals("method");
    }

    @Override
    public int getOpcode() {
        return this.instructionNode.getOpcode();
    }

    @Override
    public InstructionNode getNextInstruction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNextInstruction'");
    }

    @Override
    public InstructionNode getPreviousInstruction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPreviousInstruction'");
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
