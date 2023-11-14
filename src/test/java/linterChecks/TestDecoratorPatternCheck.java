package linterChecks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datasource.RecursiveDiver;
import datasource.StandardInput;
import domain.DecoratorCheck;
import domain.LintRunner;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestDecoratorPatternCheck {
    // check expects a list of ClassNodes to be passed in, so we'll need to do some
    // setup.
    private static final String TEST_CLASS_DIR = new File("src/test/resources/decorator-check").getAbsolutePath();
    private StandardInput reader;
    private LintRunner lr;
    private List<String> testFiles;

    @BeforeEach
    public void reset() {
        lr = new LintRunner();
        lr.addCheck(new DecoratorCheck());
        testFiles = new ArrayList<>();
    }

    @Test
    public void decoratorPatternPasses_HasAllConcreteClasses() {
        reader = new RecursiveDiver(new File(TEST_CLASS_DIR, "pass-all-types"));
        while (reader.hasNext())
            testFiles.add(reader.next());
        List<String> classPaths = lr.createClassReaders(testFiles);
        assertEquals(5, classPaths.size());

        Map<String, List<String>> results = lr.runChecks();
        List<String> text = results.get("Decorator");
        assertEquals(1, results.size());
        assertEquals(5, text.size());
        assertEquals("Potential decorator pattern classes: ", text.get(0));
        // since all of the following could be permuted, we're just interested if they
        // actually made it in.
        assertEquals(true, text.get(1).contains("AbstractComponent"));
        assertEquals(true, text.get(2).contains("AbstractComponentDecorator"));
        assertEquals(true, text.get(3).contains("ConcreteComponent"));
        assertEquals(true, text.get(4).contains("ConcreteDecoratorOne") &&
                text.get(4).contains("ConcreteDecoratorTwo"));
    }

    @Test
    public void decoratorPatternPasses_HasNoConcreteClasses() {
        reader = new RecursiveDiver(new File(TEST_CLASS_DIR, "pass-only-basics"));
        while (reader.hasNext())
            testFiles.add(reader.next());
        List<String> classPaths = lr.createClassReaders(testFiles);
        assertEquals(2, classPaths.size());

        Map<String, List<String>> results = lr.runChecks();
        List<String> text = results.get("Decorator");
        assertEquals(1, results.size());
        assertEquals(3, text.size());
        assertEquals("Potential decorator pattern classes: ", text.get(0));
        assertEquals(true, text.get(1).contains("AbstractComponent"));
        assertEquals(true, text.get(2).contains("AbstractComponentDecorator"));
    }

    @Test
    public void decoratorPatternFails_NoAbstractDecorator() {
        reader = new RecursiveDiver(new File(TEST_CLASS_DIR, "fail-missing-abstract-deco"));
        while (reader.hasNext())
            testFiles.add(reader.next());
        List<String> classPaths = lr.createClassReaders(testFiles);
        assertEquals(4, classPaths.size());

        Map<String, List<String>> results = lr.runChecks();
        List<String> text = results.get("Decorator");
        assertEquals(1, results.size());
        assertEquals(6, text.size());
        assertEquals("No strict decorator pattern found! Check to make sure you have: ", text.get(0));
        assertEquals("\t1. An abstract component (an interface)", text.get(1));
        assertEquals("\t2. An abstract decorator (an abstract class that <implements> the abstract component)",
                text.get(2));
        assertEquals(
                "\t3. (Optional) An instance of a concrete component (a concrete class that <implements> the abstract component)",
                text.get(3));
        assertEquals(
                "\t4. (Optional) An instance of a concrete decorator (a concrete class that <extends> the abstract decorator)\n",
                text.get(4));
        assertEquals(true, text.get(5).contains("AbstractComponent") &&
                text.get(5).contains("ConcreteComponent") &&
                text.get(5).contains("ConcreteDecoratorOne") &&
                text.get(5).contains("ConcreteDecoratorTwo"));
    }

    @Test
    public void decoratorPatternPasses_NoAbstractComponent() {
        reader = new RecursiveDiver(new File(TEST_CLASS_DIR, "fail-missing-abstract-component"));
        while (reader.hasNext())
            testFiles.add(reader.next());
        List<String> classPaths = lr.createClassReaders(testFiles);
        assertEquals(4, classPaths.size());

        Map<String, List<String>> results = lr.runChecks();
        List<String> text = results.get("Decorator");
        assertEquals(1, results.size());
        assertEquals(6, text.size());
        assertEquals("No strict decorator pattern found! Check to make sure you have: ", text.get(0));
        assertEquals("\t1. An abstract component (an interface)", text.get(1));
        assertEquals("\t2. An abstract decorator (an abstract class that <implements> the abstract component)",
                text.get(2));
        assertEquals(
                "\t3. (Optional) An instance of a concrete component (a concrete class that <implements> the abstract component)",
                text.get(3));
        assertEquals(
                "\t4. (Optional) An instance of a concrete decorator (a concrete class that <extends> the abstract decorator)\n",
                text.get(4));
        assertEquals(true, text.get(5).contains("AbstractComponentDecorator") &&
                text.get(5).contains("ConcreteComponent") &&
                text.get(5).contains("ConcreteDecoratorOne") &&
                text.get(5).contains("ConcreteDecoratorTwo"));
    }

    @Test
    public void decoratorPatternFails_MissingBothAbstracts() {
        reader = new RecursiveDiver(new File(TEST_CLASS_DIR, "fail-missing-both"));
        while (reader.hasNext())
            testFiles.add(reader.next());
        List<String> classPaths = lr.createClassReaders(testFiles);
        assertEquals(3, classPaths.size());

        Map<String, List<String>> results = lr.runChecks();
        List<String> text = results.get("Decorator");
        assertEquals(1, results.size());
        assertEquals(6, text.size());
        assertEquals("No strict decorator pattern found! Check to make sure you have: ", text.get(0));
        assertEquals("\t1. An abstract component (an interface)", text.get(1));
        assertEquals("\t2. An abstract decorator (an abstract class that <implements> the abstract component)",
                text.get(2));
        assertEquals(
                "\t3. (Optional) An instance of a concrete component (a concrete class that <implements> the abstract component)",
                text.get(3));
        assertEquals(
                "\t4. (Optional) An instance of a concrete decorator (a concrete class that <extends> the abstract decorator)\n",
                text.get(4));
        assertEquals(true, text.get(5).contains("ConcreteComponent") &&
                text.get(5).contains("ConcreteDecoratorOne") &&
                text.get(5).contains("ConcreteDecoratorTwo"));
    }

    @Test
    public void decoratorPatternPass_OtherClassesIncluded() {
        reader = new RecursiveDiver(new File(TEST_CLASS_DIR, "pass-all-types-extras"));
        while (reader.hasNext())
            testFiles.add(reader.next());
        List<String> classPaths = lr.createClassReaders(testFiles);
        assertEquals(6, classPaths.size());

        Map<String, List<String>> results = lr.runChecks();
        List<String> text = results.get("Decorator");
        assertEquals(1, results.size());
        assertEquals(7, text.size());
        assertEquals("Potential decorator pattern classes: ", text.get(0));
        // since all of the following could be permuted, we're just interested if they
        // actually made it in.
        assertEquals(true, text.get(1).contains("AbstractComponent"));
        assertEquals(true, text.get(2).contains("AbstractComponentDecorator"));
        assertEquals(true, text.get(3).contains("ConcreteComponent"));
        assertEquals(true, text.get(4).contains("ConcreteDecoratorOne") &&
                text.get(4).contains("ConcreteDecoratorTwo"));
        // note that line 5 is just text...
        assertEquals("\nClasses that do not participate in the decorator pattern: ", text.get(5));
        assertEquals(true, text.get(6).contains("OtherClass"));
    }
}
