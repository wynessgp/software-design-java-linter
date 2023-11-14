package presentation;

public class Main {
    public static void main(String[] args) {
        UserInterface ui = new ConsoleUserInterface(args);
        ui.display();
    }
}
