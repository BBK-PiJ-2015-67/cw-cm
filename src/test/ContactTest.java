package test;

import impl.ContactImpl;
import org.junit.Test;
import spec.Contact;

import static org.junit.Assert.assertEquals;

/**
 * Testing a Contact
 *
 * @author lmignot
 */
public class ContactTest {

    private static final int CONTACT_ID = 32;
    private static final int CONTACT_ID_NEG = -3;
    private static final int CONTACT_ID_ZERO = 0;
    private static final String CONTACT_NAME = "Master Yoda";
    private static final String CONTACT_NOTES_1 = "a comprehensive suite of tests this is, We hope.";
    private static final String CONTACT_NOTES_2 = "need more notes, Do you?";
    private static final String NOTES_DELIMITER = "\n";

    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = null;

    @Test
    public void testNewContactWithNotes () {
        Contact testContact = new ContactImpl(CONTACT_ID, CONTACT_NAME, CONTACT_NOTES_1);

        assertEquals(testContact.getId(), CONTACT_ID);
        assertEquals(testContact.getName(), CONTACT_NAME);
        assertEquals(testContact.getNotes(), CONTACT_NOTES_1);
    }

    @Test
    public void testNewContactWithNoNotes () {
        Contact testContact = new ContactImpl(CONTACT_ID, CONTACT_NAME);

        assertEquals(testContact.getId(), CONTACT_ID);
        assertEquals(testContact.getName(), CONTACT_NAME);
    }

    @Test
    public void testAddNotesToContact () {
        Contact testContact = new ContactImpl(CONTACT_ID, CONTACT_NAME);

        testContact.addNotes(CONTACT_NOTES_1);

        assertEquals(testContact.getNotes(), CONTACT_NOTES_1);
    }

    @Test
    public void testAddNotesToContactWithExistingNotes () {
        Contact testContact = new ContactImpl(CONTACT_ID, CONTACT_NAME);

        testContact.addNotes(CONTACT_NOTES_1);
        testContact.addNotes(CONTACT_NOTES_2);

        assertEquals(testContact.getNotes(), CONTACT_NOTES_1 + NOTES_DELIMITER + CONTACT_NOTES_2);
    }

    @Test
    public void testAddNotesToContactWithNotesInConstructor () {
        Contact testContact = new ContactImpl(CONTACT_ID, CONTACT_NAME, CONTACT_NOTES_1);

        testContact.addNotes(CONTACT_NOTES_2);

        assertEquals(testContact.getNotes(), CONTACT_NOTES_1 + NOTES_DELIMITER + CONTACT_NOTES_2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyNoteToContact () {
        Contact testContact = new ContactImpl(CONTACT_ID, CONTACT_NAME);
        testContact.addNotes(EMPTY_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithNegativeID () {
        new ContactImpl(CONTACT_ID_NEG, CONTACT_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithZeroID () {
        new ContactImpl(CONTACT_ID_ZERO, CONTACT_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithEmptyName () {
        new ContactImpl(CONTACT_ID, EMPTY_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testNewContactWithNullName() {
        new ContactImpl(CONTACT_ID, NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testNewContactWithNullNameAndNotes () {
        new ContactImpl(CONTACT_ID, NULL_STRING, NULL_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithEmptyNameAndNullNotes () {
        new ContactImpl(CONTACT_ID, EMPTY_STRING, NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testNewContactWithNullNotes () {
        new ContactImpl(CONTACT_ID, CONTACT_NAME, NULL_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithEmptyNotes () {
        new ContactImpl(CONTACT_ID, CONTACT_NAME, EMPTY_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullNoteToContact () {
        Contact testContact = new ContactImpl(CONTACT_ID, CONTACT_NAME);

        testContact.addNotes(NULL_STRING);
    }
}
