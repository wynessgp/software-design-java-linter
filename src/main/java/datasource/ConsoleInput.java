package datasource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConsoleInput implements StandardInput {
    private String filepath;
    private Scanner scan;

    public ConsoleInput(String filepath) {
        this.filepath = filepath;
        try {
            this.scan = new Scanner(new File(filepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String nextLine() {
        if (hasNext()) {
            return this.scan.nextLine();
        }
        return "";
    }

    public boolean hasNext() {
        return this.scan.hasNext();
    }

    public int nextInt() {
        return 1;
    }

    public String next() {
        return "";
    }
}
