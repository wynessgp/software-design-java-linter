package domain;

import java.util.ArrayList;
import java.util.List;

public class ClassNodeASM implements ClassNode {
    private org.objectweb.asm.tree.ClassNode classNode;

    public ClassNodeASM(org.objectweb.asm.tree.ClassNode classNode) {
        this.classNode = classNode;
    }

    @Override
    public List<FieldNode> getFields() {
        List<FieldNode> fields = new ArrayList<>();
        for (org.objectweb.asm.tree.FieldNode fieldNode : classNode.fields) {
            fields.add(new FieldNodeASM(fieldNode));
        }
        return fields;
    }

    @Override
    public List<MethodNode> getMethods() {
        List<MethodNode> methods = new ArrayList<>();
        for (org.objectweb.asm.tree.MethodNode methodNode : classNode.methods) {
            methods.add(new MethodNodeASM(methodNode));
        }
        return methods;
    }

    @Override
    public List<String> getInterfaces() {
        List<String> interfaces = new ArrayList<>();
        for (String s : classNode.interfaces) {
            interfaces.add(s.replace("/", "."));
        }
        return interfaces;
    }

    @Override
    public String getSuperName() {
        return classNode.superName.replace("/", ".");
    }

    @Override
    public String getClassName() {
        return classNode.name.replace("/", ".");
    }

}
