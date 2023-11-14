package datasource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileOutput implements StandardOutput {
    private BufferedWriter bufferedWriter;

    public FileOutput(String fp) {
        try {
            this.bufferedWriter = new BufferedWriter(new FileWriter(fp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(String s) throws IOException {
        this.bufferedWriter.write(s);
    }

    @Override
    public void close() throws IOException {
        this.bufferedWriter.close();
    }
}
