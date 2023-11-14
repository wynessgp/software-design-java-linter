package domain;

import org.objectweb.asm.Type;

public class FieldNodeASM extends ParamNodeASM implements FieldNode {
    private org.objectweb.asm.tree.FieldNode fieldNode;

    public FieldNodeASM(org.objectweb.asm.tree.FieldNode fieldNode) {
        this.fieldNode = fieldNode;
    }

    /**
     * @see ParamNodeASM#matchesAccess(String, int)
     */
    @Override
    public boolean matchesAccess(String access) {
        return super.matchesAccess(access, fieldNode.access);
    }

    /**
     * @see ParamNodeASM#getDesc(String)
     */
    @Override
    public String getDesc() {
        return super.getDesc(fieldNode.desc);
    }

    /**
     * @see ParamNodeASM#getName(String)
     */
    @Override
    public String getFieldName() {
        return super.getName(fieldNode.name);
    }

    @Override
    public Object getFieldValue() {
        return this.fieldNode.value;
    }

    @Override
    public String getFieldType() {
        return Type.getObjectType(this.fieldNode.desc).getClassName();
    }
}
