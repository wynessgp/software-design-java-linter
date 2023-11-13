package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LintRunner {
    private static CheckStrategy[] checkTypes =
            new CheckStrategy[] {new StaticCheck(), new FormattingCheck(), new UnusedItemsCheck(),
                    new InformationHidingCheck(), new ThreeLayerCheck(), new HollywoodCheck(),
                    new FacadeCheck(), new StrategyCheck(), new DecoratorCheck()};
    private List<CheckStrategy> strategies;
    private List<ClassReader> readers;

    public LintRunner() {
        this.strategies = new ArrayList<>();
        this.readers = new ArrayList<>();
    }

    public List<String> createClassReaders(List<String> classNames) {
        List<String> classPaths = new ArrayList<>();
        for (String className : classNames) {
            ClassReader classReader = new ClassReaderASM();
            if (classReader.acceptClass(className)) {
                readers.add(classReader);
                classPaths.add(className.substring(
                        className.lastIndexOf(className.contains("\\") ? '\\' : '/') + 1));
            }
        }
        return classPaths;
    }

    public void addCheck(CheckStrategy check) {
        this.strategies.add(check);
    }

    public Map<String, List<String>> runChecks() {
        Map<String, List<String>> results = new HashMap<>();
        List<ClassNode> classNodes = readers.stream().map(ClassReader::getClassNode).toList();
        for (CheckStrategy check : this.strategies) {
            check.performCheck(classNodes);
            results.put(check.getCheckName(), check.handleResults());
        }
        return results;
    }

    public Map<String, Object> getCheckTypes() {
        Map<String, Object> types = new HashMap<>();
        for (CheckStrategy checkType : checkTypes) {
            try {
                types.put(checkType.getCheckName(), checkType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return types;
    }

    public boolean isChecksEmpty() {
        return strategies.isEmpty();
    }
}
