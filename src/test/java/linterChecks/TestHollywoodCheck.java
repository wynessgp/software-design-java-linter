package linterChecks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datasource.RecursiveDiver;
import datasource.StandardInput;
import domain.HollywoodCheck;
import domain.LintRunner;

public class TestHollywoodCheck {
    private static final String TEST_CLASS_DIR = new File("src/test/resources/hollywood-check").getAbsolutePath();
    private StandardInput reader;
    private LintRunner lr;
    private List<String> testFiles;

    @BeforeEach
    public void reset() {
        lr = new LintRunner();
        lr.addCheck(new HollywoodCheck());
        testFiles = new ArrayList<>();
    }

    @Test
    public void hollywoodCheckNoViolationsFound() {
        reader = new RecursiveDiver(new File(TEST_CLASS_DIR, "no-violation"));
        while (reader.hasNext())
            testFiles.add(reader.next());
        List<String> classPaths = lr.createClassReaders(testFiles);
        assertEquals(2, classPaths.size());

        Map<String, List<String>> results = lr.runChecks();
        assertEquals(1, results.size());
        assertEquals(1, results.get("Hollywood").size());
        assertEquals("No violations detected", results.get("Hollywood").get(0));
    }

    @Test
    public void hollywoodCheckViolationsFound() {
        reader = new RecursiveDiver(new File(TEST_CLASS_DIR, "finds-violation"));
        while (reader.hasNext())
            testFiles.add(reader.next());
        List<String> classPaths = lr.createClassReaders(testFiles);
        assertEquals(2, classPaths.size());

        Map<String, List<String>> results = lr.runChecks();
        assertEquals(1, results.size());
        assertEquals(2, results.get("Hollywood").size());
        assertEquals("Class: hollywoodprinciple.MySubClass potentially violates the Hollywood Principle by calling the following: ",
                results.get("Hollywood").get(0));
        assertEquals(true, results.get("Hollywood").get(1).contains("super.thisIsAMethod"));
    }
}
