package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class UnusedItemsCheck implements CheckStrategy {

    // yipee, only 6 fields.
    private Set<String> analyzedClasses;
    private Map<String, Set<String>> classToUnusedFields;
    private Map<String, Set<String>> classToUnusedMethods;
    private Map<String, Set<String>> anticipatedMethodsByClass;
    private Map<String, Set<String>> anticipatedFieldsByClass;
    private Map<String, Map<String, Set<String>>> classToUnusedLocalVarsByMethod;

    public UnusedItemsCheck() {
        this.analyzedClasses = new HashSet<>();
        this.classToUnusedFields = new HashMap<>();
        this.classToUnusedMethods = new HashMap<>();
        this.classToUnusedLocalVarsByMethod = new HashMap<>();
        this.anticipatedMethodsByClass = new HashMap<>();
        this.anticipatedFieldsByClass = new HashMap<>();
    }

    @Override
    public void performCheck(List<ClassNode> classNames) {
        for (ClassNode cn : classNames) {
            analyzedClasses.add(cn.getClassName().replace("/", "."));
            parseFields(cn);
            parseMethods(cn);
        }
    }

    @Override
    public List<String> handleResults() {
        List<String> unusedItems = new ArrayList<>();
        for (String className : analyzedClasses) {
            // first: handle the issue with anticipated / unused
            // we remove any anticipated items from the used, because presumably,
            // we'd analyzed these classes at some point, too.
            if (anticipatedFieldsByClass.containsKey(className)) {
                classToUnusedFields.get(className).removeAll(anticipatedFieldsByClass.get(className));
            }
            if (anticipatedMethodsByClass.containsKey(className)) {
                classToUnusedMethods.get(className).removeAll(anticipatedMethodsByClass.get(className));
            }
            // now say things about ourselves in the return, since info is up to date.
            // if any are not empty, add on the header line.
            if (!classToUnusedFields.get(className).isEmpty() ||
                    !classToUnusedFields.get(className).isEmpty() ||
                    classToUnusedLocalVarsByMethod.get(className) != null) {
                unusedItems.add(String.format("Class: %s has unused items: ", className));
                // fields are not empty
            }
            if (!classToUnusedFields.get(className).isEmpty()) {
                unusedItems.add("\tFields: " + classToUnusedFields.get(className));
                // methods are not empty
            }
            if (!classToUnusedMethods.get(className).isEmpty()) {
                unusedItems.add("\tMethods: " + classToUnusedMethods.get(className));
            }
            // this is plain silly, but I can't think of a way around it.
            // get returns null if there's no mapping, so... idk
            if (classToUnusedLocalVarsByMethod.get(className) != null) {
                for (String methodName : classToUnusedLocalVarsByMethod.get(className).keySet()) {
                    if (!classToUnusedLocalVarsByMethod.get(className).get(methodName).isEmpty()) {
                        unusedItems.add("\tLocal vars in method " + methodName + ": " +
                                classToUnusedLocalVarsByMethod.get(className).get(methodName).toString());
                    }
                }
            }
        }

        if (unusedItems.isEmpty())
            unusedItems.add("No violations detected");

        return unusedItems;
    }

    @Override
    public String getCheckName() {
        return "Unused items";
    }

    private void parseFields(ClassNode cn) {
        for (FieldNode field : cn.getFields()) {
            // for any given field, we just add it to the class.
            addDeclaredField(cn.getClassName().replace("/", "."),
                    field.getFieldName());
        }
    }

    private void parseMethods(ClassNode cn) {
        for (MethodNode mn : cn.getMethods()) {
            // add the methods to the respective class name
            addDeclaredMethod(cn.getClassName().replace("/", "."), mn.getMethodName());
        }
        // now that all of the method names are up...
        for (MethodNode mn : cn.getMethods()) {
            parseMethodInstructions(mn, cn);
        }

    }

    private void parseMethodInstructions(MethodNode mn, ClassNode cn) {
        // we need to do some setup for local variables.
        Map<Integer, String> indexToName = new HashMap<>();
        Map<String, Integer> numTimesVariableLoaded = new HashMap<>();
        for (LocalVariableNode lvn : mn.getLocalVariables()) {
            indexToName.put(lvn.getIndex(), lvn.getName());
            numTimesVariableLoaded.put(lvn.getName(), 0);
        }

        for (InstructionNode instruction : mn.getInstructions()) {
            if (instruction.matchesInstructionType("field_insn")) {
                // ok, it's a field instruction. Cast it over.
                FieldInstructionNode fieldInstruction = instruction.toFieldInstruction();
                // now: if the owner is a class we haven't yet seen, we anticipate the field
                // later.
                // otherwise, go in and remove the field from unused.
                String fieldOwner = fieldInstruction.getFieldOwner().replace("/", ".");
                if (!analyzedClasses.contains(fieldOwner)) {
                    // put it on the anticipated list.
                    addAnticipatedField(fieldOwner, fieldInstruction.getFieldName());
                } else {
                    // we've seen the fieldOwner, so we already know it's in our declared fields.
                    // and now, it's used.
                    classToUnusedFields.get(fieldOwner).remove(fieldInstruction.getFieldName());
                }
            } else if (instruction.matchesInstructionType("method_insn")) {
                // ok, it's a method instruction. cast it over
                MethodInstructionNode methodInstruction = instruction.toMethodInstruction();
                // again, look at the method owner. if we haven't seen it before,
                // we anticipate the method declaration later.
                String methodOwner = methodInstruction.getMethodOwner().replace("/", ".");
                if (!analyzedClasses.contains(methodOwner)) {
                    // put it on the anticipated list
                    addAnticipatedMethod(methodOwner, methodInstruction.getMethodName());
                } else {
                    // we've seen the method before, now it's been used.
                    classToUnusedMethods.get(methodOwner).remove(methodInstruction.getMethodName());
                }
            } else if (instruction.matchesInstructionType("var_insn")) {
                // local variables
                // note that this also has a side effect of including looping variables.
                // so we won't remove local variables, as it's too risky.
                VarInstructionNode vin = instruction.toVarInstruction();
                // better way? i dunno.
                if (vin.getOpcode() >= 21 && vin.getOpcode() <= 25) {
                    String localName = indexToName.get(vin.getVarIndex());
                    numTimesVariableLoaded.put(localName, numTimesVariableLoaded.get(localName) + 1);
                }
            }
        }
        // do the tally for local variables again.
        for (String usedVar : numTimesVariableLoaded.keySet()) {
            // ignore it
            if (usedVar.equals("this"))
                continue;
            if (numTimesVariableLoaded.get(usedVar) < 1) {
                // then it was never loaded again (so never used).
                addUnusedLocalVariable(cn.getClassName().replace("/", "."),
                        mn.getMethodName(),
                        usedVar);
            }
        }
    }

    private void addDeclaredField(String className, String fieldName) {
        // ignore "this"
        if (fieldName.equals("this"))
            return;
        if (!classToUnusedFields.containsKey(className))
            classToUnusedFields.put(className, new HashSet<String>());
        classToUnusedFields.get(className).add(fieldName);
    }

    private void addDeclaredMethod(String className, String methodName) {
        // ignore inits.
        if (methodName.contains("init>"))
            return;
        if (!classToUnusedMethods.containsKey(className))
            classToUnusedMethods.put(className, new HashSet<String>());
        classToUnusedMethods.get(className).add(methodName);
    }

    private void addAnticipatedField(String anticipatedOwner, String anticipatedField) {
        // ignore "this"
        if (anticipatedField.equals("this"))
            return;

        if (!anticipatedFieldsByClass.containsKey(anticipatedOwner))
            anticipatedFieldsByClass.put(anticipatedOwner, new HashSet<String>());
        anticipatedFieldsByClass.get(anticipatedOwner).add(anticipatedField);
    }

    private void addAnticipatedMethod(String anticipatedOwner, String anticipatedMethod) {
        // ignore inits.
        if (anticipatedMethod.contains("init>"))
            return;

        if (!anticipatedMethodsByClass.containsKey(anticipatedOwner))
            anticipatedMethodsByClass.put(anticipatedOwner, new HashSet<String>());
        anticipatedMethodsByClass.get(anticipatedOwner).add(anticipatedMethod);
    }

    private void addUnusedLocalVariable(String className, String methodName, String localVarName) {
        if (!classToUnusedLocalVarsByMethod.containsKey(className)) {
            classToUnusedLocalVarsByMethod.put(className, new HashMap<String, Set<String>>());
            classToUnusedLocalVarsByMethod.get(className).put(methodName, new HashSet<>());
        }
        classToUnusedLocalVarsByMethod.get(className).get(methodName).add(localVarName);
    }

}
