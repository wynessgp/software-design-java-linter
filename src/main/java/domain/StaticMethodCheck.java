package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StaticMethodCheck implements CheckStrategy {
    public StaticMethodCheck() {

    }

    public void performCheck(List<String> classNames) {

    }

    public List<String> handleResults(Properties preferences) {
        return new ArrayList<>();
    }
}