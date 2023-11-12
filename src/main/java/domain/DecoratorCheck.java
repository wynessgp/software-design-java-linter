package domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

// FIXME: Bruh12

public class DecoratorCheck implements CheckStrategy {

    public void performCheck(List<ClassNode> classNames) {

    }

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

    // DONE: Graph traversal stuff
    private Map<String, Set<String>> interfaceNameToUnconfirmedConcreteComponents;
    private Map<String, Set<String>> interfaceNameToUnconfirmedAbstractDecorators;
    private Map<String, Set<String>> abstractDecoNameToUnconfirmedConcreteDecorators;

    private Set<String> outsideClasses;

    public DecoratorCheck() {
        this.abstractDecorators = (Set<String>) new HashSet<String>();
        this.concreteDecorators = (Set<String>) new HashSet<String>();
        this.abstractComponents = (Set<String>) new HashSet<String>();
        this.concreteComponents = (Set<String>) new HashSet<String>();
        this.outsideClasses = (Set<String>) new HashSet<String>();

        this.interfaceNameToUnconfirmedAbstractDecorators = (Map<String, Set<String>>) new HashMap<String, Set<String>>();
        this.interfaceNameToUnconfirmedConcreteComponents = (Map<String, Set<String>>) new HashMap<String, Set<String>>();
        this.abstractDecoNameToUnconfirmedConcreteDecorators = (Map<String, Set<String>>) new HashMap<String, Set<String>>();
    }

    public void doChecks(List<String> classesToCheck) throws IOException {
        for (String cl : classesToCheck) {
            singleClassChecks(cl);
        }
        handleResults();
    }

    public List<String> handleResults() {
        // should be at least one of both in any pattern.
        // This is the core of the "is-a", "has-a" that decorators must have.
        if (this.abstractDecorators.isEmpty() && this.concreteComponents.isEmpty()) {
            // we don't have to remove here because the pattern doesn't happen anyway, why
            // do extra work?
            System.out.println();
            System.out.println("No strict decorator pattern found! Check to make sure you have: ");
            System.out.println("        1. An abstract component (an interface)");
            System.out.println(
                    "        2. An abstract decorator (an abstract class that <implements> the abstract component)");
            System.out.println(
                    "        3. (Optional) An instance of a concrete component (a concrete class that <implements> the abstract component)");
            System.out.println(
                    "        4. (Optional) An instance of a concrete decorator (a concrete class that <extends> the abstract decorator)");
            System.out.println();
            System.out.println("Classes analyzed: " + outsideClasses.toString());
            System.out.println();
        } else {
            // removal happens before we print
            outsideClasses.removeAll(abstractDecorators);
            outsideClasses.removeAll(abstractComponents);
            outsideClasses.removeAll(concreteDecorators);
            outsideClasses.removeAll(concreteComponents);

            System.out.println();
            System.out.println("Potential decorator pattern classes: ");
            System.out.println("        abstract components: " + abstractComponents.toString());
            System.out.println("        abstract decorators: " + abstractDecorators.toString());
            System.out.println("        concrete components: " + concreteComponents.toString());
            System.out.println("        concrete decorators: " + concreteDecorators.toString());
            System.out.println();
            System.out.println("Classes that do not participate in the decorator pattern: ");
            System.out.println("        " + outsideClasses.toString());
            System.out.println();
        }
        return null; // FIXME: return list of strings
    }

    private void singleClassChecks(String classToCheck) throws IOException {
        ClassReader cr = new ClassReader(classToCheck);
        ClassNode cn = new ClassNodeASM();
        // FIXME: cr.accept(cn, ClassReader.EXPAND_FRAMES);

        // first check: is it an interface?
        // if it is, this method will add it to the abstract components list.
        if (abstractComponentCheck(cn)) {
            return;
        }

        // second check: is it the abstract decorator?
        // if it is, have the method add it to the list.
        if (abstractDecoratorCheck(cn)) {
            return;
        }

        // third check: is it a concrete component?
        // if it is, have the method add it to the list.
        if (concreteComponentCheck(cn)) {
            return;
        }

        // fourth check: is it a concrete decorator?
        // if it is, have the method add it to the list.
        if (concreteDecoratorCheck(cn)) {
            return;
        }

        // if it wasn't added from any of the above, it doesn't participate.
        // FIXME: outsideClasses.add(cn.name.replace("/", "."));

    }

    private boolean abstractComponentCheck(ClassNode cn) {
        // is the access type an interface?
        // FIXME: if ((cn.access & Opcodes.ACC_INTERFACE) != 0) {
            // get the dotted form of the class name
            // FIXME: String dottedClassName = cn.name.replace("/", ".");
            String dottedClassName = "";
            abstractComponents.add(dottedClassName);

            // graph traversal matters.
            // These are sets, so duplicated adds shouldn't matter. Plus, we only view each
            // class once.
            if (interfaceNameToUnconfirmedConcreteComponents.containsKey(dottedClassName))
                concreteComponents.addAll(interfaceNameToUnconfirmedConcreteComponents.get(dottedClassName));
            if (interfaceNameToUnconfirmedAbstractDecorators.containsKey(dottedClassName))
                abstractDecorators.addAll(interfaceNameToUnconfirmedAbstractDecorators.get(dottedClassName));
            // since we also just confirmed an abstract decorator, we need to confirm its
            // associated concrete decorators.
            for (String abstDeco : abstractDecorators) {
                if (abstractDecoNameToUnconfirmedConcreteDecorators.containsKey(abstDeco)) {
                    concreteDecorators.addAll(abstractDecoNameToUnconfirmedConcreteDecorators.get(abstDeco));
                }
            }
            // removal happens before we print

            return true;
        // }

        // return false;

        // TODO: Component that is NOT an interface??
    }

    private boolean abstractDecoratorCheck(ClassNode cn) {
        // is the access type abstract? If it isn't, we're done here.
        // FIXME: if ((cn.access & Opcodes.ACC_ABSTRACT) == 0)
            return false;
        // FIXME: String dottedClassName = cn.name.replace("/", ".");
        // does it both: implement an interface & have an instance of the same interface
        // first: do we implement at least an interface?
        // FIXME: if (cn.interfaces.isEmpty())
            // return false;

        // get dotted interface names, to be consistent
        // List<String> dottedInterfaceNames = new ArrayList<>();
        // FIXME: for (String s : cn.interfaces)
        // dottedInterfaceNames.add(s.replace("/", "."));

        // start check 2. Does it have a field that is one of the many interfaces it may
        // implement?
        // FIXME: for (FieldNode fn : cn.fields) {
        //     String userFriendlyType = y.getObjectType(fn.desc).getClassName();
        //     for (String s : dottedInterfaceNames) {
        //         // this is necessary because there are some discrepancies between the two
        //         if (userFriendlyType.contains(s)) {
        //             // then we have a matching interface. If we've already ID'd it as an abstract
        //             // component,
        //             // then this becomes an abstract decorator. If we haven't, add it as a predicted
        //             // one.
        //             if (!abstractComponents.contains(s)) {
        //                 if (!interfaceNameToUnconfirmedAbstractDecorators.containsKey(s)) {
        //                     Set<String> absDecos = new HashSet<>();
        //                     absDecos.add(dottedClassName);
        //                     interfaceNameToUnconfirmedAbstractDecorators.put(s, absDecos);
        //                 } else {
        //                     interfaceNameToUnconfirmedAbstractDecorators.get(s).add(dottedClassName);
        //                 }
        //             } else {
        //                 abstractDecorators.add(dottedClassName);
        //                 // additionally, respect graph traversal items. So, concrete decorators get
        //                 // moved, too.
        //                 concreteDecorators.addAll(abstractDecoNameToUnconfirmedConcreteDecorators.get(dottedClassName));
        //                 // remove happens before we print
        //             }
        //         }
        //     }

        // }
        // if we get down here, it may have implemented an abstract component, but
        // didn't have the field.
        // so it may very well be a concrete component.
        // return false;
    }

    private boolean concreteComponentCheck(ClassNode cn) {
        // make sure it's not an interface, or an abstract class
        // if ((cn.access & Opcodes.ACC_ABSTRACT) != 0)
        //     return false;
        // if ((cn.access & Opcodes.ACC_INTERFACE) != 0)
        //     return false;
        // String dottedName = cn.name.replace("/", ".");
        // // ok, it's not an interface or an abstract class. Now, see if it works with an
        // // abstract component.
        // // do we implement an interface?
        // if (cn.interfaces.isEmpty())
        //     return false;

        // for (String interfaceName : cn.interfaces) {
        //     String dottedInterName = interfaceName.replace("/", ".");
        //     if (abstractComponents.contains(dottedInterName)) {
        //         // we've found a confirmed abstractComponent that we implement.
        //         concreteComponents.add(dottedName);
        //         // we can prematurely end here since it's confirmed.
        //         return true;
        //     } else {
        //         // it may be true that we are a concrete component for ANY interface
        //         // so we need to mark ourselves for each and every one
        //         // sure, it's a bit inefficient, but the graph traversal respects that we'll
        //         // only mark for the correct interface.
        //         if (!interfaceNameToUnconfirmedConcreteComponents.containsKey(dottedInterName)) {
        //             Set<String> concretes = new HashSet<>();
        //             concretes.add(dottedName);
        //             interfaceNameToUnconfirmedConcreteComponents.put(dottedInterName, concretes);
        //         } else {
        //             interfaceNameToUnconfirmedConcreteComponents.get(dottedInterName).add(dottedName);
        //         }
        //     }
        // }
        return false;

        // TODO: Classes that extend non-interface components??
    }

    private boolean concreteDecoratorCheck(ClassNode cn) {
        // same abstract/interface check
        // FIXME: if ((cn.access & Opcodes.ACC_ABSTRACT) != 0)
        //     return false;
        // if ((cn.access & Opcodes.ACC_INTERFACE) != 0)
        //     return false;
        // String dottedName = cn.name.replace("/", ".");
        // // does it extend a class?
        // if (cn.superName.isEmpty())
        //     return false;
        // // a class can only extend one other one in Java. If it isn't a confirmed
        // // abstractDecorator,
        // // say that we're expecting an abstractDecorator with the given superClass's
        // // name.
        // String dottedSuperName = cn.superName.replace("/", ".");
        // silly edge case...
        // if (dottedSuperName.equals("java.lang.Object"))
        //     return false;
        // if (abstractDecorators.contains(dottedSuperName)) {
        //     // then we extend this decorator.
        //     concreteDecorators.add(dottedName);
        //     return true;
        // } else {
        //     // we extend a class, but we don't know if that class is an abstract decorator
        //     // yet
        //     // so we need to mark it off as having potential, and if that gets confirmed,
        //     // then we are also confirmed as a concrete decorator.
        //     if (!abstractDecoNameToUnconfirmedConcreteDecorators.containsKey(dottedSuperName)) {
        //         Set<String> concreteDecos = new HashSet<>();
        //         concreteDecos.add(dottedName);
        //         abstractDecoNameToUnconfirmedConcreteDecorators.put(dottedSuperName, concreteDecos);
        //     } else {
        //         abstractDecoNameToUnconfirmedConcreteDecorators.get(dottedSuperName).add(dottedName);
        //     }
        // }

        return false;
    }

    @Override
    public String getCheckName() {
        return "Decorator";
    }
}