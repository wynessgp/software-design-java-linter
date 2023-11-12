package presentation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import datasource.RecursiveDiver;
import datasource.StandardInput;
import datasource.StandardOutput;
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

    private void promptForWorkingDirectory() {
        switch (args.length) {
            case 0:
                System.out.println("Please enter the path to your project directory");
                projectDirectory = new File(userInput.getNextLine());
                break;
            case 1:
                System.out.println("Detected command line argument");
                projectDirectory = new File(args[0]);
                break;
            default:
                System.out.println("Too many command line arguments");
                System.out.println("Usage: java -jar linter.jar <projectDirectory>");
                System.exit(1);
        }
        if (!projectDirectory.exists()) {
            System.out.println("The specified directory does not exist");
            System.exit(1);
        }
        displayDetectedFiles();
    }

    private void displayDetectedFiles() {
        List<String> classPaths = new ArrayList<>();
        StringBuilder sb = new StringBuilder("Found: ");
        this.reader = new RecursiveDiver(projectDirectory);
        while (reader.hasNext()) {
            String next = reader.next();
            classPaths.add(next);
            sb.append(next + " ");
        }
        if (classPaths.size() > 0) {
            System.out.println(sb);
        } else {
            System.out.println("No classes found in " + projectDirectory.toString());
            System.exit(1);
        }
        this.runner = new LintRunner(classPaths);
        promptForCheckOptions();
    }

    private void promptForCheckOptions() {
        runChecks();
    }
    
    private void runChecks() {
        this.runner.runChecks();
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
