package datasource;

import java.io.IOException;

public interface StandardOutput {
    public void write(String s) throws IOException;

    public void close() throws IOException;
}
