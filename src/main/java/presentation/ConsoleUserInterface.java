package presentation;

import java.io.File;
import java.util.List;

import datasource.StandardInput;
import datasource.StandardOutput;
import domain.CheckStrategy;

public class ConsoleUserInterface implements UserInterface {
    private UserInput userInput;
    private File projectDirectory;
    private List<CheckStrategy> checksToRun;
    private StandardInput reader;
    private StandardOutput writer;

    @Override
    public void display() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'display'");
    }

    private void promptForWorkingDirectory() {

    }

    private void displayDetectedFiles() {

    }

    private void promptForCheckOptions() {

    }

    private void displayCheckResults() {
        
    }

    private void promptForSavingResults() {

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
