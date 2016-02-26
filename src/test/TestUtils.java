package test;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Methods for handling common functionality between tests
 * (eg. deleting resources etc...)
 */
public abstract class TestUtils {

    private static final String FILENAME = "contacts.txt";


    /**
     * Deletes the ContactManager data file<br>
     * The file is assumed to be named "contacts.txt"
     */
    static void deleteDataFile() {
        try {
            Path p = FileSystems.getDefault().getPath(FILENAME);
            if (Files.exists(p)) {
                Files.delete(p);
            }
        } catch (AccessDeniedException secEx) {
            System.err.println("Permission denied while attempting to delete " + FILENAME);
            secEx.printStackTrace();
        } catch (IOException ioEx) {
            System.err.println("I/O error occurred while attempting to delete " + FILENAME);
            ioEx.printStackTrace();
        }
    }
}
