package linterChecks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import datasource.RecursiveDiver;
import datasource.StandardInput;
import domain.FacadeCheck;
import domain.LintRunner;

public class TestFacadePatternCheck {
    private static final String TEST_CLASS_DIRECTORY =
            new File("src/test/resources/facade-check/").getAbsolutePath();
    private StandardInput reader;
    private LintRunner runner;
    private List<String> testFiles;

    @BeforeEach
    public void reset() {
        runner = new LintRunner();
        runner.addCheck(new FacadeCheck());
        testFiles = new ArrayList<>();
    }

    @Test
    public void testFacadePatternCheckPatternFound() {
        // Add test files
        reader = new RecursiveDiver(new File(TEST_CLASS_DIRECTORY, "pattern-found"));
        while (reader.hasNext()) {
            testFiles.add(reader.next());
        }
        List<String> classPaths = runner.createClassReaders(testFiles);
        assertEquals(4, classPaths.size());

        // Run and parse checks
        Map<String, List<String>> results = runner.runChecks();
        assertEquals(1, results.size());

        // Sort results for consistency
        String[] facadeResults = results.get("Facade").toArray(new String[0]);
        Arrays.sort(facadeResults);
        assertEquals(2, facadeResults.length);
        assertEquals("domain.MaybeFacadeClass is possibly a facade class", facadeResults[0]);
        assertEquals("presentation.MyMainClass is possibly a facade class", facadeResults[1]);
    }

    @Test
    public void testFacadePatternCheckMainClassOnly() {
        // Add test files
        reader = new RecursiveDiver(new File(TEST_CLASS_DIRECTORY, "main-only"));
        while (reader.hasNext()) {
            testFiles.add(reader.next());
        }
        List<String> classPaths = runner.createClassReaders(testFiles);
        assertEquals(4, classPaths.size());

        // Run and parse checks
        Map<String, List<String>> results = runner.runChecks();
        assertEquals(1, results.size());
        assertEquals(1, results.get("Facade").size());
        assertEquals("presentation.MyMainClass is possibly a facade class",
                results.get("Facade").get(0));
    }
}
