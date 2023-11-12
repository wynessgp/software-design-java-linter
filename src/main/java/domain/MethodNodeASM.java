package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class MethodNodeASM implements MethodNode {
    private org.objectweb.asm.tree.MethodNode methodNode;

    public MethodNodeASM(org.objectweb.asm.tree.MethodNode methodNode) {
        this.methodNode = methodNode;
    }

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
        return accessLevel == methodNode.access;
    }

    @Override
    public String getDesc() {
        return this.methodNode.desc.replace("/", ".");
    }

    @Override
    public String getMethodName() {
        return this.methodNode.name.replace("/", ".");
    }

    @Override
    public List<InstructionNode> getInstructions() {
        List<InstructionNode> instructions = new ArrayList<>();
        for (org.objectweb.asm.tree.AbstractInsnNode instruct : this.methodNode.instructions) {
            instructions.add(new InstructionNodeASM(instruct));
        }
        return instructions;
    }

    @Override
    public String getReturnType() {
        return Type.getReturnType(this.methodNode.desc).getClassName();
    }

    @Override
    public Map<String, String> getArgs() {
        Map<String, String> namesToArgTypes = new HashMap<>();

        List<String> localVarNames = new ArrayList<>();
        for (org.objectweb.asm.tree.LocalVariableNode vn : this.methodNode.localVariables) {
            if (!vn.name.equals("this")) // ignore in case of static contexts. Type also ignores it.
                localVarNames.add(vn.name);
        }
        int curIndex = 0;
        for (Type argType : Type.getArgumentTypes(this.methodNode.desc)) {
            namesToArgTypes.put(localVarNames.get(curIndex), argType.getClassName().replace("/", "."));
            curIndex++;
        }

        return namesToArgTypes;
    }

}
