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
import domain.LintRunner;
import domain.UnusedItemsCheck;

public class TestUnusedItemsCheck {
    private static final String TEST_CLASS_DIR = new File("src/test/resources/unused-items-check").getAbsolutePath();
    private StandardInput reader;
    private LintRunner lr;
    private List<String> testFiles;

    @BeforeEach
    public void reset() {
        lr = new LintRunner();
        lr.addCheck(new UnusedItemsCheck());
        testFiles = new ArrayList<>();
    }

    @Test 
    public void unusedItemsCheckNoViolationsFound() {
        reader = new RecursiveDiver(new File(TEST_CLASS_DIR, "no-violation"));
        while (reader.hasNext())
            testFiles.add(reader.next());
        List<String> classPaths = lr.createClassReaders(testFiles);
        assertEquals(1, classPaths.size());

        Map<String, List<String>> results = lr.runChecks();
        assertEquals(1, results.size());
        assertEquals(1, results.get("Unused items").size());
        assertEquals("No violations detected", results.get("Unused items").get(0));
    }

    @Test 
    public void unusedItemsCheckViolationsFound() {
        reader = new RecursiveDiver(new File(TEST_CLASS_DIR, "finds-violation"));
        while (reader.hasNext())
            testFiles.add(reader.next());
        List<String> classPaths = lr.createClassReaders(testFiles);
        assertEquals(2, classPaths.size());

        Map<String, List<String>> results = lr.runChecks();
        List<String> text = results.get("Unused items");
        assertEquals(1, results.size());
        assertEquals(6, text.size());
        // first line should say there's an issue with otherClass
        assertEquals(true, text.get(0).contains("OtherClass"));
        // second line should say it's with notUsedElsewhere
        assertEquals(true, text.get(1).contains("notUsedElsewhere"));
        // third line should say that anotherUnusedMethod and unusedMethodInOtherClass are not used...
        assertEquals(true, text.get(2).contains("anotherUnusedMethod") &&
                            text.get(2).contains("unusedMethodInOtherClass"));
        // fourth line should start detailing stuff about MySillyClass
        assertEquals(true, text.get(3).contains("MySillyClass"));
        // fifth line should say stuff about unusedField
        assertEquals(true, text.get(4).contains("unusedField"));
        // sixth line should say stuff about anotherUnusedMethod, unusedMethod
        assertEquals(true, text.get(5).contains("anotherUnusedMethod") && 
                            text.get(5).contains("unusedMethod"));
    }


}
