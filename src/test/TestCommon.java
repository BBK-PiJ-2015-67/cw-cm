package test;

import spec.Contact;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Methods for handling common functionality within package {@code test}
 * Common declared constants to avoid repetition and ensure consistency in tests
 */
final class TestCommon {

    private static final String FILENAME = "contacts.txt";

    static final int CONTACT_1_ID = 1;
    static final int CONTACT_2_ID = 2;
    static final int CONTACT_3_ID = 3;
    static final int CONTACT_4_ID = 4;
    static final String CONTACT_1_NAME = "Wade Wilson";
    static final String CONTACT_2_NAME = "Peter Quill";
    static final String CONTACT_3_NAME = "Jessica Jones";
    static final String CONTACT_4_NAME = "Bruce Wayne";
    static final String CONTACT_1_NOTES = "Deadpool";
    static final String CONTACT_2_NOTES = "Starlord";
    static final String CONTACT_3_NOTES = "No nickname";
    static final String CONTACT_4_NOTES = "Batman";

    static final String CONTACT_3_NAME_WHITESPACE = "Jessica  Jones";
    static final String CONTACT_3_NAME_PREFIX = " Jessica Jones";
    static final String CONTACT_3_NAME_SUFFIX = "Jessica Jones ";

    static final int MEETING_ID = 458;

    static final int EXPECTED_CONTACT_SET_SIZE = 4;
    static final String NON_EXISTENT_CONTACT_NAME = "Matt Murdock";

    static final int FUTURE_YEAR = 2020;
    static final int FUTURE_MONTH = 5;
    static final int FUTURE_DAY = 16;
    static final int FUTURE_HOUR_11 = 11;
    static final int FUTURE_HOUR_9 = 9;
    static final int FUTURE_HOUR_14 = 14;
    static final int FUTURE_MINUTE_20 = 20;
    static final int FUTURE_MINUTE_15 = 15;
    static final int FUTURE_MINUTE_35 = 35;
    static final int PAST_YEAR = 2007;
    static final int PAST_MONTH = 8;
    static final int PAST_DAY = 2;
    static final int YEAR = 1967;
    static final int MONTH = 4;
    static final int DAY = 11;

    static final int ID_NEG = -2;
    static final int ID_ZERO = 0;

    static final int ILLEGAL_ID_1 = 6;
    static final int ILLEGAL_ID_2 = 90;
    static final int ILLEGAL_ID_3 = 43;
    static final int ILLEGAL_ID_4 = 987;

    static final String NOTES_1 = "Notes";
    static final String NOTES_2 = "Some more notes.";
    static final String CONTACT_NOTES_DELIMITER = "\n";

    static final int EMPTY_SIZE = 0;
    static final String EMPTY_STRING = "";
    static final String NULL_STRING = null;
    static final String NULL_NOTES = null;
    static final Calendar NULL_CAL = null;
    static final int[] NULL_INT_ARRAY = null;
    static final Set<Contact> NULL_CONTACTS = null;
    static final Set<Contact> EMPTY_CONTACTS = new HashSet<>();

    /**
     * Deletes the ContactManager data file<br>
     * The file is assumed to be named "contacts.txt"
     */
    static void deleteDataFile () {
        try {
            Path p = FileSystems.getDefault().getPath(FILENAME);
            if (Files.exists(p)) {
                Files.delete(p);
            }
        } catch (AccessDeniedException adEx) {
            System.err.println("Access denied while attempting to delete " + FILENAME);
            adEx.printStackTrace();
        } catch (IOException ioEx) {
            System.err.println("I/O error occurred while attempting to delete " + FILENAME);
            ioEx.printStackTrace();
        }
    }
}
