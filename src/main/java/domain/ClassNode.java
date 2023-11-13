package domain;

import java.util.List;

public interface ClassNode {
    public List<FieldNode> getFields();

    public List<MethodNode> getMethods();

    public List<String> getInterfaces();

    public String getSuperName();

    public String getClassName();

    public boolean matchesAccess(String access);
}
