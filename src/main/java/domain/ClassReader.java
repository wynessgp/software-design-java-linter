package domain;

public interface ClassReader {
    public boolean acceptClass(String className);

    public ClassNode getClassNode();
}
