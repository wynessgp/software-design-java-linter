package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Type;

public class DecoratorCheck implements CheckStrategy {

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
    // First map: "This is a concrete class, and it implements an interface. However,
    // I haven't yet seen if the interface is actually an abstract
    // component, so I'll hold off until the interface confirms me,
    // if it appears."
    private Map<String, Set<String>> interfaceNameToUnconfirmedConcreteComponents;

    // Second map: "This is an abstract class, and it both implements and has a field
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
            singleClassCheck(cn);
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
        if (this.abstractDecorators.isEmpty() && this.abstractComponents.isEmpty()) {
            ret.add("\nNo strict decorator pattern found! Check to make sure you have: \n");
            ret.add("\t\t1. An abstract component (an interface)\n");
            ret.add("\t\t2. An abstract decorator (an abstract class that <implements> the abstract component)\n");
            ret.add("\t\t3. (Optional) An instance of a concrete component (a concrete class that <implements> the abstract component)\n");
            ret.add("\t\t4. (Optional) An instance of a concrete decorator (a concrete class that <extends> the abstract decorator)\n\n");
            ret.add("Classes analyzed: " + outsideClasses.toString() + "\n");
        } else {
            // removal happens before we print, if we have the pattern.
            outsideClasses.removeAll(abstractDecorators);
            outsideClasses.removeAll(abstractComponents);
            outsideClasses.removeAll(concreteDecorators);
            outsideClasses.removeAll(concreteComponents);
            ret.add("Potential decorator pattern classes: \n");
            ret.add("\t\tAbstract components: " + abstractComponents.toString() + "\n");
            ret.add("\t\tConcrete components: " + concreteComponents.toString() + "\n");
            ret.add("\t\tAbstract decorators: " + abstractDecorators.toString() + "\n");
            ret.add("\t\tConcrete decorators: " + concreteDecorators.toString() + "\n\n");
            ret.add("Classes that do not participate in the decorator pattern: \n");
            ret.add("\t\t" + outsideClasses.toString() + "\n");
        }
        return ret;
    }

    private void singleClassCheck(ClassNode cn) {
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
        outsideClasses.add(cn.getClassName());
    }

    private boolean abstractComponentCheck(ClassNode cn) {
        // is the access type an interface?
        if (cn.matchesAccess("interface")) {
            // get the dotted form of the class name
            String dottedClassName = cn.getClassName();
            abstractComponents.add(dottedClassName);

            // graph traversal matters.
            // These are sets, so duplicated adds shouldn't matter. Plus, we only view each
            // class once.
            if (interfaceNameToUnconfirmedConcreteComponents.containsKey(dottedClassName))
                concreteComponents
                        .addAll(interfaceNameToUnconfirmedConcreteComponents.get(dottedClassName));
            if (interfaceNameToUnconfirmedAbstractDecorators.containsKey(dottedClassName))
                abstractDecorators
                        .addAll(interfaceNameToUnconfirmedAbstractDecorators.get(dottedClassName));
            // since we also just confirmed an abstract decorator, we need to confirm its
            // associated concrete decorators.
            for (String abstDeco : abstractDecorators) {
                if (abstractDecoNameToUnconfirmedConcreteDecorators.containsKey(abstDeco)) {
                    concreteDecorators
                            .addAll(abstractDecoNameToUnconfirmedConcreteDecorators.get(abstDeco));
                }
            }
            // removal happens before we print
            return true;
        }

        return false;

    }

    private boolean abstractDecoratorCheck(ClassNode cn) {
        // is the access type abstract? If it isn't, we're done here.
        if (!cn.matchesAccess("abstract"))
            return false;
        // next check relies on having an interface as a field, and implementing it.
        // so, check to see if it implements any interfaces.
        if (cn.getInterfaces().isEmpty())
            return false;

        // start check 2. Does it have a field that is one of the many interfaces it may
        // implement?
        List<String> dottedInterfaceNames = cn.getInterfaces();
        String dottedClassName = cn.getClassName();
        for (FieldNode fn : cn.getFields()) {
            String userFriendlyType = Type.getObjectType(fn.getDesc()).getClassName();
            for (String s : dottedInterfaceNames) {
                // this is necessary because there are some discrepancies between the two
                if (userFriendlyType.contains(s)) {
                    // then we have a matching interface. If we've already ID'd it as an abstract
                    // component,
                    // then this becomes an abstract decorator. If we haven't, add it as a predicted
                    // one.
                    if (!abstractComponents.contains(s)) {
                        if (!interfaceNameToUnconfirmedAbstractDecorators.containsKey(s)) {
                            Set<String> absDecos = new HashSet<>();
                            absDecos.add(dottedClassName);
                            interfaceNameToUnconfirmedAbstractDecorators.put(s, absDecos);
                        } else {
                            interfaceNameToUnconfirmedAbstractDecorators.get(s)
                                    .add(dottedClassName);
                        }
                    } else {
                        abstractDecorators.add(dottedClassName);
                        // additionally, respect graph traversal items. So, concrete decorators get
                        // moved, too.
                        concreteDecorators.addAll(abstractDecoNameToUnconfirmedConcreteDecorators
                                .get(dottedClassName));
                        // remove happens before we print
                        return true;
                    }
                }
            }

        }
        // if we get down here, it may have implemented an abstract component, but
        // didn't have the field.
        // so it may very well be a concrete component.
        return false;
    }

    private boolean concreteComponentCheck(ClassNode cn) {
        // make sure it's not an interface, or an abstract class
        if (cn.matchesAccess("interface"))
            return false;
        if (cn.matchesAccess("abstract"))
            return false;
        // ok, it's not an interface or an abstract class. Now, see if it works with an
        // abstract component.
        // do we implement an interface?
        if (cn.getInterfaces().isEmpty())
            return false;

        String dottedName = cn.getClassName();

        for (String interfaceName : cn.getInterfaces()) {
            if (abstractComponents.contains(interfaceName)) {
                // we've found a confirmed abstractComponent that we implement.
                concreteComponents.add(dottedName);
                // we can prematurely end here since it's confirmed.
                return true;
            } else {
                // it may be true that we are a concrete component for ANY interface
                // so we need to mark ourselves for each and every one
                // sure, it's a bit inefficient, but the graph traversal respects that we'll
                // only mark for the correct interface.
                if (!interfaceNameToUnconfirmedConcreteComponents.containsKey(interfaceName)) {
                    Set<String> concretes = new HashSet<>();
                    concretes.add(dottedName);
                    interfaceNameToUnconfirmedConcreteComponents.put(interfaceName, concretes);
                } else {
                    interfaceNameToUnconfirmedConcreteComponents.get(interfaceName).add(dottedName);
                }
            }
        }
        return false;

    }

    private boolean concreteDecoratorCheck(ClassNode cn) {
        // same abstract/interface check
        if (cn.matchesAccess("interface"))
            return false;
        if (cn.matchesAccess("abstract"))
            return false;
        // does it extend a class?
        if (cn.getSuperName().isEmpty())
            return false;
        // a class can only extend one other one in Java. If it isn't a confirmed
        // abstractDecorator,
        // say that we're expecting an abstractDecorator with the given superClass's
        // name.
        String dottedName = cn.getClassName();
        String dottedSuperName = cn.getSuperName();
        // silly edge case...
        if (dottedSuperName.equals("java.lang.Object"))
            return false;
        if (abstractDecorators.contains(dottedSuperName)) {
            // then we extend this decorator.
            concreteDecorators.add(dottedName);
            return true;
        } else {
            // we extend a class, but we don't know if that class is an abstract decorator
            // so we need to mark it off as having potential, and if that gets confirmed,
            // then we are also confirmed as a concrete decorator.
            if (!abstractDecoNameToUnconfirmedConcreteDecorators.containsKey(dottedSuperName)) {
                Set<String> concreteDecos = new HashSet<>();
                concreteDecos.add(dottedName);
                abstractDecoNameToUnconfirmedConcreteDecorators.put(dottedSuperName, concreteDecos);
            } else {
                abstractDecoNameToUnconfirmedConcreteDecorators.get(dottedSuperName)
                        .add(dottedName);
            }
        }

        return false;
    }
}
