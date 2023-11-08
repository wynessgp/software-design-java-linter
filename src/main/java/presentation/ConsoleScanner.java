package presentation;

import java.util.Scanner;

public class ConsoleScanner implements UserInput {
    private Scanner scanner;

    public ConsoleScanner() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getNextLine() {
        return scanner.nextLine();
    }

}
