package domain;

import java.util.List;

public class FacadeCheck implements CheckStrategy {
    public FacadeCheck() {

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
        return "Facade";
    }
}