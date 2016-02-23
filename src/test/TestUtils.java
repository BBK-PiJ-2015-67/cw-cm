package test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Methods for handling common functionality between tests
 * (eg. deleting resources etc...)
 */
public class TestUtils {

    private static final String FILENAME = "contacts.txt";

    public static void deleteCMDataFile () {
        try {
            Path p = FileSystems.getDefault().getPath(FILENAME);
            if (Files.exists(p)) {
                Files.delete(p);
            }
        } catch (SecurityException secEx) {
            System.err.println("Permission denied while attempting to delete " + FILENAME);
            secEx.printStackTrace();
        } catch (IOException ioEx) {
            System.err.println("I/O error occurred while attempting to delete " + FILENAME);
            ioEx.printStackTrace();
        }
    }
}
