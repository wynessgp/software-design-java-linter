package presentation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import datasource.FileOutput;
import datasource.RecursiveDiver;
import datasource.StandardInput;
import datasource.StandardOutput;
import domain.CheckStrategy;
import domain.LintRunner;

public class ConsoleUserInterface implements UserInterface {
    private static final String RESULTS_OUTPUT_FILE = "results.txt";
    private String[] args;
    private UserInput userInput;
    private File projectDirectory;
    private StandardInput reader;
    private StandardOutput writer;
    private LintRunner runner;
    private Map<String, List<String>> results;
    private List<String> classes;

    public ConsoleUserInterface(String[] args) {
        this.args = args;
        this.userInput = new ConsoleScanner();
        this.runner = new LintRunner();
        this.writer = new FileOutput(RESULTS_OUTPUT_FILE);
        this.results = new HashMap<>();
    }

    @Override
    public void display() {
        System.out.println("Welcome to Lint Boss");
        promptForWorkingDirectory();
    }

    /**
     * Ensures a directory with .class files is provided. If an argument is passed in, check the
     * path. Otherwise, prompt the user for a working directory.
     * 
     * @see ConsoleScanner ConsoleScanner (Handles user input)
     */
    private void promptForWorkingDirectory() {
        switch (args.length) {
            case 0:
                System.out.println("Please enter the path to your project directory");
                projectDirectory = new File(userInput.getNextLine());
                break;
            default:
                System.out.println("Detected directory as command line argument");
                projectDirectory = new File(args[0]);
                break;
        }
        if (!projectDirectory.exists()) {
            System.out.println("The specified directory does not exist");
            System.exit(1);
        }
        displayDetectedFiles();
    }

    /**
     * Perform the recursive file search using a StandardInput reader. Additionally process each
     * file's bytecode for further analysis.
     * 
     * @see datasource.RecursiveDiver
     */
    private void displayDetectedFiles() {
        List<String> classPaths = new ArrayList<>();
        this.reader = new RecursiveDiver(projectDirectory);
        while (reader.hasNext()) {
            String next = reader.next();
            classPaths.add(next);
        }
        if (classPaths.size() == 0) {
            System.out.println("No classes found in " + projectDirectory.toString());
            System.exit(1);
        }
        StringBuilder sb = new StringBuilder("Found: ");
        this.classes = runner.createClassReaders(classPaths);
        for (String path : classes) {
            sb.append(path).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        System.out.println(sb.toString() + "\n");
        promptForCheckOptions();
    }

    /**
     * Ask the user which checks they would like to run.
     * 
     * @see domain.LintRunner#checkTypes checkTypes (Required list of check instances)
     * @see domain.LintRunner#addCheck(CheckStrategy)
     */
    private void promptForCheckOptions() {
        Map<String, Object> checkTypes = runner.getCheckTypes();
        if (this.args.length < 2) {
            List<String> continueOptions = Arrays.asList(new String[] {"y", ""});
            System.out.println("Please select the checks you would like to perform:");
            for (String check : checkTypes.keySet()) {
                System.out.print("\t" + check + " check? [Y/n] ");
                if (continueOptions.contains(userInput.getNextLine().toLowerCase())) {
                    runner.addCheck((CheckStrategy) checkTypes.get(check));
                }
            }
            if (runner.isChecksEmpty()) {
                System.out.println("No checks selected");
                System.exit(1);
            }
        } else {
            String[] checksFromArgs = Arrays.copyOfRange(args, 1, args.length);
            System.out.println("Detected checks in command line arguments");
            for (String arg : checksFromArgs) {
                arg = arg.substring(0, 1).toUpperCase()
                        + arg.substring(1).replace("-", " ").toLowerCase();
                if (!checkTypes.containsKey(arg)) {
                    System.out.println(arg + " check does not exist");
                    System.exit(1);
                }
                runner.addCheck((CheckStrategy) checkTypes.get(arg));
                System.out.println(arg + " check added");
            }
        }
        runChecks();
    }

    /**
     * Tell the runner to process the bytecode and return the results. Displays the check results to
     * the user.
     * 
     * @see domain.LintRunner#runChecks()
     */
    private void runChecks() {
        System.out.print("Press enter to run checks");
        userInput.getNextLine();
        this.results = this.runner.runChecks();
        System.out.println("\n----- Results -----");
        for (String resultName : this.results.keySet()) {
            System.out.println(resultName + ": ");
            if (results.get(resultName).isEmpty()) {
                // Each check should return a "base" result if the check didn't find anything. This
                // is here to catch any instances where the results are empty.
                System.out.println("- No results");
            } else {
                for (String result : this.results.get(resultName)) {
                    System.out.println("- " + result);
                }
            }
        }
        promptForSavingResults();
        promptForUmlGeneration();
    }

    /**
     * Ask the user if they would like to save the linting results to a predetermined file.
     */
    private void promptForSavingResults() {
        System.out.print("\nWould you like to save the results? [y/N] ");
        if (userInput.getNextLine().toLowerCase().equals("y")) {
            saveResults();
        }
    }

    /**
     * Format and save the linting results.
     */
    private void saveResults() {
        try {
            StringBuilder sb = new StringBuilder("----- Lint Boss -----\nClasses checked:\n");
            for (String className : classes) {
                sb.append(className).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("\n\nCheck results:\n");
            for (String resultName : results.keySet()) {
                sb.append(resultName).append("\n");
                for (String result : results.get(resultName)) {
                    sb.append("- ").append(result).append("\n");
                }
                sb.append("\n");
            }
            sb.delete(sb.length() - 1, sb.length());
            writer.write(sb.toString());
            System.out
                    .println("Results saved to " + new File(RESULTS_OUTPUT_FILE).getAbsolutePath());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving results");
            System.exit(1);
        }
    }

    private void promptForUmlGeneration() {
        System.out.println("\nUML generation not yet implemented");
    }
}
