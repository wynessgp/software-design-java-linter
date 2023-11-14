package domain;

import java.util.List;
import java.util.Map;

public interface MethodNode {
    public boolean matchesAccess(String access);
    public String getDesc();
    public String getMethodName();
    public List<LocalVariableNode> getLocalVariables();
    public List<InstructionNode> getInstructions();
    public String getReturnType();
    public Map<String, String> getArgs();
    public List<String> getArgTypes();
}
