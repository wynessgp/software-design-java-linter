package presentation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import datasource.RecursiveDiver;
import datasource.StandardInput;
import datasource.StandardOutput;
import domain.CheckStrategy;
import domain.LintRunner;

public class ConsoleUserInterface implements UserInterface {
    private String[] args;
    private UserInput userInput;
    private File projectDirectory;
    private StandardInput reader;
    private StandardOutput writer;
    private LintRunner runner;

    public ConsoleUserInterface(String[] args) {
        this.args = args;
        this.userInput = new ConsoleScanner();
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
                System.out.println("Detected command line argument");
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
        this.runner = new LintRunner();
        for (String path : runner.createClassReaders(classPaths)) {
            sb.append(path).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        System.out.println(sb.toString());
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
            System.out.println("\nPlease select the checks you would like to perform:");
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
        Map<String, List<String>> results = this.runner.runChecks();
        for (String resultName : results.keySet()) {
            System.out.print(resultName + ": ");
            System.out.println(results.get(resultName).isEmpty() ? "No results" : "");
            for (String result : results.get(resultName)) {
                System.out.println("\t" + result);
            }
        }
        promptForSavingResults();
        promptForCodeCleanup();
        promptForUmlGeneration();
        promptForSkeletonCodeGeneration();
    }

    private void promptForSavingResults() {
        saveResults();
    }

    private void saveResults() {

    }

    private void promptForCodeCleanup() {

    }

    private void promptForUmlGeneration() {

    }

    private void promptForSkeletonCodeGeneration() {

    }

}
