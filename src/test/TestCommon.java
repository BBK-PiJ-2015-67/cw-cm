package test;

import impl.ContactImpl;
import spec.Contact;
import spec.ContactManager;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains:<br>
 * <ul>
 * <li>Constants to avoid repetition and ensure consistency in tests</li>
 * <li>Methods for handling common functionality in tests</li>
 * </ul>
 */
final class TestCommon {

    private TestCommon () { }

    static final String FILENAME = "contacts.txt";

    static final int CONTACT_1_ID = 1;
    static final int CONTACT_2_ID = 2;
    static final int CONTACT_3_ID = 3;
    static final int CONTACT_4_ID = 4;
    static final int CONTACT_5_ID = 5;
    static final int CONTACT_6_ID = 6;
    static final String CONTACT_1_NAME = "Wade Wilson";
    static final String CONTACT_2_NAME = "Peter Quill";
    static final String CONTACT_3_NAME = "Jessica Jones";
    static final String CONTACT_4_NAME = "Bruce Wayne";
    static final String CONTACT_5_NAME = "Peter Parker";
    static final String CONTACT_6_NAME = "Superman";
    static final String CONTACT_1_NOTES = "Deadpool";
    static final String CONTACT_2_NOTES = "Starlord";
    static final String CONTACT_3_NOTES = "No nickname";
    static final String CONTACT_4_NOTES = "Batman";
    static final String CONTACT_5_NOTES = "Spiderman";
    static final String CONTACT_6_NOTES = "Clark Kent";

    static final String CONTACT_3_NAME_WHITESPACE = "Jessica  Jones";
    static final String CONTACT_3_NAME_PREFIX = " Jessica Jones";
    static final String CONTACT_3_NAME_SUFFIX = "Jessica Jones ";
    static final String CONTACT_3_NAME_EXTENDED = "Jessica Jones - Metahuman";

    static final int NUM_CONTACTS_DEFAULT = 6;
    static final int DUPLICATE_CONTACTS = 3;
    static final int SIMILAR_CONTACTS = 5;
    static final String NON_EXISTENT_CONTACT_NAME = "Matt Murdock";

    static final int MEETING_ID = 458;
    static final int FIRST_MEETING_ID = 1;
    static final int SECOND_MEETING_ID = 2;

    static final String MEETING_NOTES = "Notes";
    static final String MEETING_NOTES_2 = "More Notes";
    static final String MEETING_NOTES_3 = "More than more Notes";
    static final String NOTES_DELIMITER = "\n";

    static final int FUTURE_YEAR = 2020;
    static final int FUTURE_MONTH = 5;
    static final int FUTURE_DAY = 16;
    static final int HOUR_11 = 11;
    static final int HOUR_9 = 9;
    static final int HOUR_14 = 14;
    static final int MINUTE_20 = 20;
    static final int MINUTE_15 = 15;
    static final int MINUTE_35 = 35;
    static final int PAST_YEAR = 1967;
    static final int PAST_MONTH = 8;
    static final int PAST_DAY = 2;
    static final int YEAR = 1967;
    static final int MONTH = 4;
    static final int DAY = 11;

    static final int THIRTY_SECONDS = 30;
    static final int ONE_MILLISECOND = 1;
    static final int FIVE_MILLISECONDS = 5;
    static final int MINUS_FIVE_MILLISECONDS = -5;

    static final int ILLEGAL_ID_1 = 11;
    static final int ILLEGAL_ID_2 = 90;
    static final int ILLEGAL_ID_3 = 43;
    static final int ILLEGAL_ID_4 = 987;
    static final int ID_NEG = -2;
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;
    static final int THREE = 3;
    static final int FOUR = 4;
    static final int FIVE = 5;
    static final int SIX = 6;
    static final int SEVEN = 7;
    static final int EIGHT = 8;
    static final int NINE = 9;
    static final int ELEVEN = 11;

    static final int EMPTY_SIZE = 0;
    static final String EMPTY_STRING = "";
    static final String NULL_STRING = null;
    static final Calendar NULL_CAL = null;
    static final int[] NULL_INT_ARRAY = null;
    static final Contact NULL_CONTACT = null;
    static final Set<Contact> NULL_CONTACTS = null;
    static final Set<Contact> EMPTY_CONTACTS = new HashSet<>();

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
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    /**
     * Returns a Set of contacts for testing purposes.
     *
     * @return The set of test contacts
     */
    static Set<Contact> getMeetingContacts() {
        Set<Contact> meetingContacts = new HashSet<>();
        meetingContacts.add(new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_2_ID, CONTACT_2_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_3_ID, CONTACT_3_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_4_ID, CONTACT_4_NAME));
        return meetingContacts;
    }

    /**
     * Adds test contacts to a Contact Manager instance for testing
     * Some tests require a CM to have contacts &ndash; this method avoids
     * code duplication in tests
     *
     * @param cm The CM instance to which contacts will be added
     */
    static void addTestContacts(ContactManager cm) {
        cm.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);
        cm.addNewContact(CONTACT_2_NAME, CONTACT_2_NOTES);
        cm.addNewContact(CONTACT_3_NAME, CONTACT_3_NOTES);
        cm.addNewContact(CONTACT_4_NAME, CONTACT_4_NOTES);
        cm.addNewContact(CONTACT_5_NAME, CONTACT_5_NOTES);
        cm.addNewContact(CONTACT_6_NAME, CONTACT_6_NOTES);
    }

    /**
     * Adds meetings to a Contact Manager instance for testing
     * As some tests require a CM instance to have a mixed set of meetings
     * &ndash; this method helps to avoid code duplication in tests
     *
     * @param cm The CM instance to which meetings will be added
     */
    static void addTestMeetings(ContactManager cm, Set<Contact> contactsA, Set<Contact> contactsB) {
        Calendar futureDate = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY);
        Calendar futureDate1 = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY, HOUR_9, MINUTE_15);
        Calendar futureDate2 = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY, HOUR_11, MINUTE_20);
        Calendar futureDate3 = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY, HOUR_14, MINUTE_35);

        Calendar pastDate = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY);
        Calendar pastDate1 = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY, HOUR_9, MINUTE_15);
        Calendar pastDate2 = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY, HOUR_11, MINUTE_20);
        Calendar pastDate3 = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY, HOUR_14, MINUTE_35);

        // mixed set of meetings for standard tests
        cm.addFutureMeeting(contactsA, futureDate);
        cm.addNewPastMeeting(contactsA, pastDate, MEETING_NOTES);
        cm.addNewPastMeeting(contactsA, pastDate1, MEETING_NOTES);
        cm.addFutureMeeting(contactsA, futureDate1);
        cm.addFutureMeeting(contactsA, futureDate2);
        cm.addNewPastMeeting(contactsA, pastDate2, MEETING_NOTES);
        cm.addFutureMeeting(contactsA, futureDate3);
        cm.addNewPastMeeting(contactsA, pastDate3, MEETING_NOTES);

        // for duplication tests
        cm.addFutureMeeting(contactsB, futureDate);
        cm.addFutureMeeting(contactsB, futureDate);
        cm.addNewPastMeeting(contactsB, pastDate, MEETING_NOTES);
        cm.addNewPastMeeting(contactsB, pastDate, MEETING_NOTES);
    }
}
