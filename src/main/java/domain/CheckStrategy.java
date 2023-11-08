package domain;

import java.util.List;
import java.util.Properties;

public interface CheckStrategy {
    public void performCheck(List<String> classNames);
    public List<String> handleResults(Properties preferences);
}