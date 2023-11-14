package domain;

public class LocalVariableNodeASM extends ParamNodeASM implements LocalVariableNode {
    private org.objectweb.asm.tree.LocalVariableNode localVariableNode;

    public LocalVariableNodeASM(org.objectweb.asm.tree.LocalVariableNode localVariableNode) {
        this.localVariableNode = localVariableNode;
    }

    /**
     * @see ParamNodeASM#getDesc(String)
     */
    @Override
    public String getDesc() {
        return super.getDesc(localVariableNode.desc);
    }

    @Override
    public int getIndex() {
        return this.localVariableNode.index;
    }

    /**
     * @see ParamNodeASM#getName(String)
     */
    @Override
    public String getName() {
        return super.getName(localVariableNode.name);
    }

}
