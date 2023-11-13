package datasource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileReader extends FileInputStream {
    public FileReader(File file) throws FileNotFoundException {
        super(file);
    }
}
