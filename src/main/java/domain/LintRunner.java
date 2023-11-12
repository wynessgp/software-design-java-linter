package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LintRunner {
    private List<CheckStrategy> strategies;
    private List<ClassReader> readers;

    public LintRunner(List<String> classNames) {
        this.strategies = new ArrayList<>();
        this.readers = new ArrayList<>();
        createClassReaders(classNames);
    }

    private void createClassReaders(List<String> classNames) {
        for (String className : classNames) {
            ClassReader classReader = new ClassReaderASM();
            if (classReader.acceptClass(className)) {
                readers.add(classReader);
            }
        }
    }

    public void addCheck(CheckStrategy check) {
        this.strategies.add(check);
    }

    public Map<String, List<String>> runChecks() {
        Map<String, List<String>> results = new HashMap<>();
        List<ClassNode> classNodes = readers.stream()
                .map(ClassReader::getClassNode).toList();
        for (CheckStrategy check : this.strategies) {
            check.performCheck(classNodes);
            results.put(check.getCheckName(), check.handleResults());
        }
        return results;
    }
}
