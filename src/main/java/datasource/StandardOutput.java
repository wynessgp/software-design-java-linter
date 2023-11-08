package datasource;

import java.io.IOException;

/**
 * StandardOutput
 */
public interface StandardOutput {
    public void write(String s, int off, int len) throws IOException;
    public void write(int c) throws IOException;
}