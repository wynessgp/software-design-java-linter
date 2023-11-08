package domain;

import java.util.Map;

public interface MethodNode {
    public boolean matchesAccess(String access);
    public String getDesc();
    public String getMethodName();
    public InstructionNode getInstructions();
    public String getReturnType();
    public Map<String, String> getArgs();
}
