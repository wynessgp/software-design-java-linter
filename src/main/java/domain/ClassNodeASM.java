package domain;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;

public class ClassNodeASM implements ClassNode {
    private org.objectweb.asm.tree.ClassNode classNode;

    public ClassNodeASM() {
        this.classNode = new org.objectweb.asm.tree.ClassNode();
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
    public boolean isInterface() {
        return (classNode.access & Opcodes.ACC_INTERFACE) != 0;
    }

    @Override
    public boolean isAbstract() {
        return (classNode.access & Opcodes.ACC_ABSTRACT) != 0;
    }

    @Override
    public String getSuperName() {
        return classNode.superName.replace("/", ".");
    }

    @Override
    public String getClassName() {
        return classNode.name.replace("/", ".");
    }

    /**
     * Checks for the access level of the class. Takes in a string of space-separated modifiers and
     * returns true if they match the integer access level.
     */
    @Override
    public boolean matchesAccess(String access) {
        int accessLevel = 0;
        for (String a : access.split(" ")) {
            switch (a.toLowerCase()) {
                case "public":
                    accessLevel += Opcodes.ACC_PUBLIC;
                    break;
                case "private":
                    accessLevel += Opcodes.ACC_PRIVATE;
                    break;
                case "protected":
                    accessLevel += Opcodes.ACC_PROTECTED;
                    break;
                case "static":
                    accessLevel += Opcodes.ACC_STATIC;
                    break;
                case "final":
                    accessLevel += Opcodes.ACC_FINAL;
                    break;
                case "abstract":
                    accessLevel += Opcodes.ACC_ABSTRACT;
                    break;
                case "interface":
                    accessLevel += Opcodes.ACC_INTERFACE;
                    break;
            }
        }
        return accessLevel == classNode.access;
    }

    public org.objectweb.asm.tree.ClassNode getAsmNode() {
        return classNode;
    }

}
