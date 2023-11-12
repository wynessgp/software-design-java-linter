package domain;

import java.util.List;

public class HollywoodCheck implements CheckStrategy {
    public HollywoodCheck() {

    }

    @Override
    public void performCheck(List<ClassNode> classNames) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performCheck'");
    }

    @Override
    public List<String> handleResults() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleResults'");
    }

    @Override
    public String getCheckName() {
        return "Hollywood";
    }
}