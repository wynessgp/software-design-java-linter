package datasource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileOutput implements StandardOutput {
    private String filepath;
    private BufferedWriter bufferedWriter;

    public FileOutput(String fp) {
        this.filepath = fp;
        try {
            this.bufferedWriter = new BufferedWriter(new FileWriter(fp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String s, int off, int len) throws IOException {
        this.bufferedWriter.write(s, off, len);
    }

    public void write(int c) throws IOException {
        this.bufferedWriter.write(c);
    }
}