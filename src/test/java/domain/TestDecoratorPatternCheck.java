package domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class TestDecoratorPatternCheck {

    // check expects a list of ClassNodes to be passed in, so we'll need to do some setup. 
    private static List<String> testFiles;
    private static LintRunner lr;


    @BeforeAll 
    public static void setup() {
        lr = new LintRunner();
        CheckStrategy dc = new DecoratorCheck();

        testFiles = new ArrayList<>();
        testFiles.add("resources.decorator-check.AbstractComponent");
        testFiles.add("resources.decorator-check.ConcreteComponent");
        testFiles.add("resources.decorator-check.AbstractComponentDecorator");
        testFiles.add("resources.decorator-check.ConcreteDecoratorOne");
        testFiles.add("resources.decorator-check.ConcreteDecoratorTwo");

        lr.addCheck(dc);
    }
    
    @Test
    public void decoratorPatternPasses_HasConcreteClasses_HappyPath() {
        lr.createClassReaders(testFiles);
        List<String> res = lr.runChecks().get("Decorator");

        assertEquals(res.get(0), "Potential decorator pattern classes: \n");
        assertEquals(res.get(1), "\t\tAbstract components: [decorator-check.AbstractComponent] \n");
        assertEquals(res.get(2), "\t\tConcrete components: [decorator-check.ConcreteComponent] \n");
        assertEquals(res.get(3), "\t\tAbstract decorators: [decorator-check.AbstractComponentDecorator] \n");
        assertEquals(res.get(4), "\t\tConcrete decorators: [decorator-check.ConcreteDecoratorOne, decorator-check.ConcreteDecoratorTwo]\n");
        assertEquals(res.get(5), "Classes that do not participate in the decorator pattern: \n");
        assertEquals(res.get(6), "\t\t[]\n");
    }

    @Test
    public void decoratorPatternPasses_HasConcreteClasses_AbstractComponentLast() {

    }

    @Test
    public void decoratorPatternPasses_HasConcreteClasses_AbstractDecoratorLast() {

    }

    @Test
    public void decoratorPatternPasses_NoConcreteClasses() {

    }

    @Test
    public void decoratorPatternFails_MissingAbstractComponent() {

    }

    @Test
    public void decoratorPatternFails_MissingAbstractDecorator() {

    }

    @Test
    public void decoratorPatternFails_MissingBothAbstracts() {

    }
    
    
}