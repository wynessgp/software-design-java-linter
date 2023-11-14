package domain;

public class LocalVariableNodeASM implements LocalVariableNode {
    private org.objectweb.asm.tree.LocalVariableNode localVariableNode;

    public LocalVariableNodeASM(org.objectweb.asm.tree.LocalVariableNode localVariableNode) {
        this.localVariableNode = localVariableNode;
    }

    @Override
    public String getDesc() {
        return this.localVariableNode.desc.replace("/", ".");
    }

    @Override
    public int getIndex() {
        return this.localVariableNode.index;
    }

    @Override
    public String getName() {
        return this.localVariableNode.name;
    }
    
}
