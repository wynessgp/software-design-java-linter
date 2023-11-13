package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InformationHidingCheck implements CheckStrategy {
    private Set<String[]> violations;

    public InformationHidingCheck() {
        this.violations = new HashSet<>();
    }

    @Override
    public void performCheck(List<ClassNode> classNames) {
        for (ClassNode classNode : classNames) {
            parseFields(classNode);
        }
    }

    private void parseFields(ClassNode classNode) {
        for (FieldNode field : classNode.getFields()) {
            if (field.matchesAccess("public")) {
                violations.add(new String[] {classNode.getClassName(), field.getFieldName()});
            }
        }
    }

    @Override
    public List<String> handleResults() {
        List<String> informationHidingViolations = new ArrayList<>();
        if (violations.isEmpty()) {
            informationHidingViolations.add("No violations detected");
        } else {
            for (String[] violation : violations) {
                informationHidingViolations.add(
                        String.format("Class %s has public field %s", violation[0], violation[1]));
            }
        }
        return informationHidingViolations;
    }

    @Override
    public String getCheckName() {
        return "Information hiding";
    }
}
