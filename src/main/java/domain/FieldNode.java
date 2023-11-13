package domain;

public interface FieldNode {
    public String getDesc();
    
    public String getFieldName();
    
    public Object getFieldValue();
    
    public String getFieldType();
    
    public boolean matchesAccess(String access);
}
