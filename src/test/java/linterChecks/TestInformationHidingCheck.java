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
import domain.InformationHidingCheck;
import domain.LintRunner;

public class TestInformationHidingCheck {
    private static final String TEST_CLASS_DIRECTORY =
            new File("src/test/resources/information-hiding-check/").getAbsolutePath();
    private StandardInput reader;
    private LintRunner runner;
    private List<String> testFiles;

    @BeforeEach
    public void reset() {
        runner = new LintRunner();
        runner.addCheck(new InformationHidingCheck());
        testFiles = new ArrayList<>();
    }

    @Test
    public void testInformationHidingCheckNoViolationsFound() {
        // Add test files
        reader = new RecursiveDiver(new File(TEST_CLASS_DIRECTORY, "success"));
        while (reader.hasNext()) {
            testFiles.add(reader.next());
        }
        List<String> classPaths = runner.createClassReaders(testFiles);
        assertEquals(2, classPaths.size());

        // Run and parse checks
        Map<String, List<String>> results = runner.runChecks();
        assertEquals(1, results.size());
        assertEquals(1, results.get("Information hiding").size());
        assertEquals("No violations detected", results.get("Information hiding").get(0));
    }

    @Test
    public void testInformationHidingCheckViolationFound() {
        // Add test files
        reader = new RecursiveDiver(new File(TEST_CLASS_DIRECTORY, "fail"));
        while (reader.hasNext()) {
            testFiles.add(reader.next());
        }
        List<String> classPaths = runner.createClassReaders(testFiles);
        assertEquals(2, classPaths.size());

        // Run and parse checks
        Map<String, List<String>> results = runner.runChecks();
        assertEquals(1, results.size());
        assertEquals(1, results.get("Information hiding").size());
        assertEquals("Class MySecondClass has public field myInt",
                results.get("Information hiding").get(0));
    }
}
