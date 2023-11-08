package domain;

import java.util.List;

public class ClassNodeASM implements ClassNode {
    private org.objectweb.asm.tree.ClassNode classNode;

    public ClassNodeASM(org.objectweb.asm.tree.ClassNode classNode) {
        this.classNode = classNode;
    }

    @Override
    public List<FieldNode> getFields() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFields'");
    }

    @Override
    public List<MethodNode> getMethods() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMethods'");
    }

    @Override
    public List<String> getInterfaces() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInterfaces'");
    }

    @Override
    public String getSuperName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSuperName'");
    }

    @Override
    public String getClassName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClassName'");
    }

}
