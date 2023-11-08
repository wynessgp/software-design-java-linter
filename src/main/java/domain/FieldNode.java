package domain;

public interface FieldNode {
    public boolean matchesAccess(String access);
    public String getDesc();
    public String getFieldName();
    public Object getFieldValue();
    public String getFieldType();
}
