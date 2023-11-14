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
    // adding interface support. If I implement an interface,
    // and its methods get used, then mine should be at the end, as well.
    private Map<String, Set<String>> classToImplementedInterfaces;

    public UnusedItemsCheck() {
        this.analyzedClasses = new HashSet<>();
        this.classToUnusedFields = new HashMap<>();
        this.classToUnusedMethods = new HashMap<>();
        this.anticipatedMethodsByClass = new HashMap<>();
        this.anticipatedFieldsByClass = new HashMap<>();
        this.classToImplementedInterfaces = new HashMap<>();
    }

    @Override
    public void performCheck(List<ClassNode> classNames) {
        for (ClassNode cn : classNames) {
            analyzedClasses.add(cn.getClassName().replace("/", "."));
            classToImplementedInterfaces.put(cn.getClassName().replace("/", "."), new HashSet<String>());
            for (String interfaceName : cn.getInterfaces()) {
                classToImplementedInterfaces.get(cn.getClassName().replace("/", "."))
                        .add(interfaceName.replace("/", "."));
            }
            parseFields(cn);
            parseMethods(cn);
        }
    }

    @Override
    public List<String> handleResults() {
        List<String> unusedItems = new ArrayList<>();
        for (String className : analyzedClasses) {
            // first: handle interfaces. If their methods are used, and we implement that
            // interface,
            // then our methods are also used (presumably).
            if (classToImplementedInterfaces.containsKey(className)) {
                Set<String> implementedInterfaces = classToImplementedInterfaces.get(className);
                // out of all of the interfaces we implement, have we analyzed one?
                for (String inter : implementedInterfaces) {
                    if (analyzedClasses.contains(inter)) {
                        // if we did, then check to see if there are any of interface's methods
                        // on the anticipated list. if there are, we can remove them for this
                        // class, too.
                        if (anticipatedMethodsByClass.get(inter) != null && 
                            !anticipatedMethodsByClass.get(inter).isEmpty()) {
                            classToUnusedMethods.get(className).removeAll(anticipatedMethodsByClass.get(inter));
                        }
                    }
                }
            }
            // second: handle the issue with anticipated / unused
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
                    !classToUnusedMethods.get(className).isEmpty()) {
                unusedItems.add(String.format("Potentially unused items for class %s: ", className));
                // fields are not empty
            }
            if (!classToUnusedFields.get(className).isEmpty()) {
                unusedItems.add("\tFields: " + classToUnusedFields.get(className));
                // methods are not empty
            }
            if (!classToUnusedMethods.get(className).isEmpty()) {
                unusedItems.add("\tMethods: " + classToUnusedMethods.get(className));
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
        // adding a blank set here to make parsing results easier
        // it's not guaranteed that addDeclaredField makes one because it may
        // not get called at all
        classToUnusedFields.put(cn.getClassName().replace("/", "."), new HashSet<String>());
        for (FieldNode field : cn.getFields()) {
            // for any given field, we just add it to the class.
            addDeclaredField(cn.getClassName().replace("/", "."),
                    field.getFieldName());
        }
    }

    private void parseMethods(ClassNode cn) {
        // adding a blank set again to make parsing easier
        // same reason, but for addDeclaredMethod here.
        classToUnusedMethods.put(cn.getClassName().replace("/", ","), new HashSet<String>());
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
        for (InstructionNode instruction : mn.getInstructions()) {
            if (instruction.matchesInstructionType("field_insn")) {
                // ok, it's a field instruction. Cast it over.
                FieldInstructionNode fieldInstruction = instruction.toFieldInstruction();
                // say that we've now seen the field, and associate it with its owner.
                String fieldOwner = fieldInstruction.getFieldOwner().replace("/", ".");
                addAnticipatedField(fieldOwner, fieldInstruction.getFieldName());
            } else if (instruction.matchesInstructionType("method_insn")) {
                // ok, it's a method instruction. cast it over
                MethodInstructionNode methodInstruction = instruction.toMethodInstruction();
                // say that we've now seen the method, and associate it with its owner.
                String methodOwner = methodInstruction.getMethodOwner().replace("/", ".");
                addAnticipatedMethod(methodOwner, methodInstruction.getMethodName());
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
        // ignore inits, main
        if (methodName.contains("init>") || methodName.equals("main"))
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

}
