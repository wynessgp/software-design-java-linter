package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Type;

public class DecoratorCheck implements CheckStrategy {

    private Set<String> analyzedClasses;
    // The abstract decorator should be the one that both:
    // 1. Has an interface as a field
    // 2. Implements the interface
    private Set<String> abstractDecorators;

    // Anything that extends at least one abstract decorator
    private Set<String> concreteDecorators;

    // A component is usually represented with an interface in Java
    private Set<String> abstractComponents;
    // these will implement the interface, but not have the interface as a field.
    private Set<String> concreteComponents;

    // Graph traversal stuff
    // First map: "This is a concrete class, and it implements an interface.
    // However,
    // I haven't yet seen if the interface is actually an abstract
    // component, so I'll hold off until the interface confirms me,
    // if it appears."
    private Map<String, Set<String>> interfaceNameToUnconfirmedConcreteComponents;

    // Second map: "This is an abstract class, and it both implements and has a
    // field
    // of the interface's type, but we haven't yet seen that interface as
    // an actual abstract component, so I'll hold off until the interface
    // confirms me, if it appears."
    private Map<String, Set<String>> interfaceNameToUnconfirmedAbstractDecorators;

    // Third map: "This is a concrete class, and it extends another class. However,
    // I haven't yet seen the class it extends, so I'll hold off until
    // the abstract decorator confirms me, if it appears."
    private Map<String, Set<String>> abstractDecoNameToUnconfirmedConcreteDecorators;

    private Set<String> outsideClasses;

    public DecoratorCheck() {
        this.analyzedClasses = new HashSet<>();
        this.abstractDecorators = new HashSet<>();
        this.concreteDecorators = new HashSet<>();
        this.abstractComponents = new HashSet<>();
        this.concreteComponents = new HashSet<>();
        this.outsideClasses = new HashSet<>();

        this.interfaceNameToUnconfirmedAbstractDecorators = new HashMap<>();
        this.interfaceNameToUnconfirmedConcreteComponents = new HashMap<>();
        this.abstractDecoNameToUnconfirmedConcreteDecorators = new HashMap<>();
    }

    @Override
    public void performCheck(List<ClassNode> classNames) {
        for (ClassNode cn : classNames) {
            parseForParticipationInPattern(cn);
        }
    }

    @Override
    public String getCheckName() {
        return "Decorator";
    }

    public List<String> handleResults() {
        List<String> ret = new ArrayList<>();
        // should be at least one of both in any pattern.
        // This is the core of the "is-a", "has-a" that decorators must have.
        if (this.abstractDecorators.isEmpty() || this.abstractComponents.isEmpty()) {
            ret.add("No strict decorator pattern found! Check to make sure you have: ");
            ret.add("\t1. An abstract component (an interface)");
            ret.add("\t2. An abstract decorator (an abstract class that <implements> the abstract component)");
            ret.add("\t3. (Optional) An instance of a concrete component (a concrete class that <implements> the abstract component)");
            ret.add("\t4. (Optional) An instance of a concrete decorator (a concrete class that <extends> the abstract decorator)\n");
            ret.add("Classes analyzed: " + analyzedClasses.toString());
        } else {
            // removal happens before we print, if we have the pattern.
            outsideClasses.removeAll(abstractDecorators);
            outsideClasses.removeAll(abstractComponents);
            outsideClasses.removeAll(concreteDecorators);
            outsideClasses.removeAll(concreteComponents);
            ret.add("Potential decorator pattern classes: ");
            ret.add("\tAbstract components: " + abstractComponents.toString());
            ret.add("\tAbstract decorators: " + abstractDecorators.toString());
            if (!concreteComponents.isEmpty())
                ret.add("\tConcrete components: " + concreteComponents.toString());
            if (!concreteDecorators.isEmpty())
                ret.add("\tConcrete decorators: " + concreteDecorators.toString());
            if (!outsideClasses.isEmpty()) {
                ret.add("\nClasses that do not participate in the decorator pattern: ");
                ret.add("\t" + outsideClasses.toString());
            }
        }
        return ret;
    }

    private void parseForParticipationInPattern(ClassNode cn) {

        analyzedClasses.add(cn.getClassName().replace("/", "."));
        // first check: is it an interface?
        // if it is, this method will add it to the abstract components list.
        if (abstractComponentCheck(cn))
            return;

        // second check: is it the abstract decorator?
        // if it is, have the method add it to the list.
        if (abstractDecoratorCheck(cn))
            return;

        // third check: is it a concrete component?
        // if it is, have the method add it to the list.
        if (concreteComponentCheck(cn))
            return;

        // fourth check: is it a concrete decorator?
        // if it is, have the method add it to the list.
        if (concreteDecoratorCheck(cn)) {
            return;
        }

        // if it wasn't added from any of the above, it doesn't participate.
        outsideClasses.add(cn.getClassName().replace("/", "."));
    }

    private boolean abstractComponentCheck(ClassNode cn) {
        // is the access type an interface?
        if (cn.isInterface()) {
            // get the dotted form of the class name
            String dottedClassName = cn.getClassName().replace("/", ".");
            abstractComponents.add(dottedClassName);
            // graph traversal matters.
            confirmConcreteComponentsForClass(dottedClassName);
            confirmAbstractDecoratorsForClass(dottedClassName);
            // if we just confirmed an abstract decorator, we need to confirm
            // the previously unconfirmed concrete decorators.
            for (String abstDeco : abstractDecorators) {
                confirmConcreteDecoratorsForClass(abstDeco);
            }
            return true;
        }

        return false;
    }

    private boolean abstractDecoratorCheck(ClassNode cn) {
        // is the access type abstract? If it isn't, we're done here.
        if (!cn.isAbstract())
            return false;
        // next sub-check relies on having an interface as a field, and implementing it.
        // so, check to see if it implements any interfaces.
        if (cn.getInterfaces().isEmpty())
            return false;

        // start sub-check 2. Does it have a field that is one of the many interfaces it
        // may
        // implement?
        List<String> interfaceNames = cn.getInterfaces();
        String className = cn.getClassName().replace("/", ".");

        for (FieldNode fn : cn.getFields()) {
            // get the type of the field.
            String userFriendlyType = Type.getObjectType(fn.getDesc()).getClassName();
            // check our interfaces that we have.
            for (String s : interfaceNames) {
                String interfaceName = s.replace("/", ".");
                // this is necessary because there are some discrepancies between the two
                if (!userFriendlyType.contains(interfaceName))
                    continue;
                // then we have a matching interface, so this is an abstract deco.
                // is there an abstract component already confirmed?
                if (!abstractComponents.contains(interfaceName)) {
                    addUnconfirmedAbstractDecoForInterface(interfaceName, className);
                } else {
                    // there is already an abstract component.
                    abstractDecorators.add(className);
                    confirmConcreteDecoratorsForClass(className);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean concreteComponentCheck(ClassNode cn) {
        // make sure it's not an interface, or an abstract class
        if (cn.isInterface())
            return false;
        if (cn.isAbstract())
            return false;
        // ok, it's not an interface or an abstract class.
        // do we implement an interface?
        if (cn.getInterfaces().isEmpty())
            return false;

        String className = cn.getClassName().replace("/", ".");
        for (String s : cn.getInterfaces()) {
            String interfaceName = s.replace("/", ".");
            if (abstractComponents.contains(interfaceName)) {
                // we've found a confirmed abstractComponent that we implement.
                concreteComponents.add(className);
                return true;
            } else {
                // graph traversal setup.
                addUnconfirmedConcreteComponentForInterface(interfaceName, className);
            }
        }
        return false;
    }

    private boolean concreteDecoratorCheck(ClassNode cn) {
        // same abstract/interface check
        if (cn.isInterface())
            return false;
        if (cn.isAbstract())
            return false;
        // does it extend a class?
        if (cn.getSuperName().isEmpty())
            return false;
        String className = cn.getClassName().replace("/", ".");
        String superClassName = cn.getSuperName().replace("/", ".");
        // all classes extend object, we're not necessarily interested in that.
        if (superClassName.equals("java.lang.Object"))
            return false;
        if (abstractDecorators.contains(superClassName)) {
            // then we extend this decorator.
            concreteDecorators.add(className);
            return true;
        } else {
            // we extend a class, but we don't know if that class is an abstract decorator
            // so we need to mark it off as having potential, and if that gets confirmed,
            // then we are also confirmed as a concrete decorator.
            addUnconfirmedConcreteDecoForAbstractDeco(superClassName, className);
        }

        return false;
    }

    private void confirmConcreteComponentsForClass(String className) {
        // if there were unconfirmed concrete components on this name, they are now
        // confirmed.
        if (interfaceNameToUnconfirmedConcreteComponents.containsKey(className))
            concreteComponents
                    .addAll(interfaceNameToUnconfirmedConcreteComponents.get(className));
    }

    private void confirmAbstractDecoratorsForClass(String className) {
        // if there were unconfirmed abstract decos on this name, they are now
        // confirmed.
        if (interfaceNameToUnconfirmedAbstractDecorators.containsKey(className))
            abstractDecorators
                    .addAll(interfaceNameToUnconfirmedAbstractDecorators.get(className));
    }

    private void confirmConcreteDecoratorsForClass(String className) {
        // if there were unconfirmed concrete decos on this name, they are now
        // confirmed.
        if (abstractDecoNameToUnconfirmedConcreteDecorators.containsKey(className))
            concreteDecorators
                    .addAll(abstractDecoNameToUnconfirmedConcreteDecorators.get(className));
    }

    private void addUnconfirmedAbstractDecoForInterface(String interfaceName, String absDecoName) {
        if (!interfaceNameToUnconfirmedAbstractDecorators.containsKey(interfaceName)) {
            interfaceNameToUnconfirmedAbstractDecorators.put(interfaceName, new HashSet<String>());
        }
        interfaceNameToUnconfirmedAbstractDecorators.get(interfaceName).add(absDecoName);
    }

    private void addUnconfirmedConcreteComponentForInterface(String interfaceName, String concreteComponentName) {
        if (!interfaceNameToUnconfirmedConcreteComponents.containsKey(interfaceName)) {
            interfaceNameToUnconfirmedConcreteComponents.put(interfaceName, new HashSet<String>());
        }
        interfaceNameToUnconfirmedConcreteComponents.get(interfaceName).add(concreteComponentName);
    }

    private void addUnconfirmedConcreteDecoForAbstractDeco(String absDecoName, String concDecoName) {
        if (!abstractDecoNameToUnconfirmedConcreteDecorators.containsKey(absDecoName)) {
            abstractDecoNameToUnconfirmedConcreteDecorators.put(absDecoName, new HashSet<String>());
        }
        abstractDecoNameToUnconfirmedConcreteDecorators.get(absDecoName).add(concDecoName);
    }
}
