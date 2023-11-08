package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DecoratorCheck implements CheckStrategy {
    public DecoratorCheck() {

    }

    public void performCheck(List<String> classNames) {

    }

    public List<String> handleResults(Properties preferences) {
        return new ArrayList<>();
    }
}