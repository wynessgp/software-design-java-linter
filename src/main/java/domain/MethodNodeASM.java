package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Type;

public class MethodNodeASM extends ParamNodeASM implements MethodNode {
    private org.objectweb.asm.tree.MethodNode methodNode;

    public MethodNodeASM(org.objectweb.asm.tree.MethodNode methodNode) {
        this.methodNode = methodNode;
    }

    /**
     * @see ParamNodeASM#matchesAccess(String, int)
     */
    @Override
    public boolean matchesAccess(String access) {
        return super.matchesAccess(access, methodNode.access);
    }

    /**
     * @see ParamNodeASM#getDesc(String)
     */
    @Override
    public String getDesc() {
        return super.getDesc(methodNode.desc);
    }

    /**
     * @see ParamNodeASM#getName(String)
     */
    @Override
    public String getMethodName() {
        return super.getName(methodNode.name);
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
            namesToArgTypes.put(localVarNames.get(curIndex),
                    argType.getClassName().replace("/", "."));
            curIndex++;
        }

        return namesToArgTypes;
    }

    @Override
    public List<String> getArgTypes() {
        return Arrays.asList(Type.getArgumentTypes(this.methodNode.desc)).stream()
                .map(Type::getClassName).toList();
    }

    @Override
    public List<LocalVariableNode> getLocalVariables() {
        List<LocalVariableNode> localVars = new ArrayList<>();
        if (this.methodNode.localVariables != null) {
            for (org.objectweb.asm.tree.LocalVariableNode lvn : this.methodNode.localVariables) {
                localVars.add(new LocalVariableNodeASM(lvn));
            }
        }
        return localVars;
    }
}
