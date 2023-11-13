package domain;

import java.util.List;

public interface CheckStrategy {
    public void performCheck(List<ClassNode> classNames);

    public List<String> handleResults();

    public String getCheckName();
}
