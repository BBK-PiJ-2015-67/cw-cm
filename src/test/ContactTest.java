package test;

import impl.ContactImpl;
import org.junit.Test;
import spec.Contact;

import static org.junit.Assert.assertEquals;
import static test.TestCommon.*;

/**
 * Testing a Contact
 *
 * @author lmignot
 */
public class ContactTest {

    @Test
    public void testNewContactWithNotes () {
        Contact testContact = new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME, MEETING_NOTES_1);

        assertEquals(testContact.getId(), CONTACT_1_ID);
        assertEquals(testContact.getName(), CONTACT_1_NAME);
        assertEquals(testContact.getNotes(), MEETING_NOTES_1);
    }

    @Test
    public void testNewContactWithNoNotes () {
        Contact testContact = new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME);

        assertEquals(testContact.getId(), CONTACT_1_ID);
        assertEquals(testContact.getName(), CONTACT_1_NAME);
    }

    @Test
    public void testAddNotesToContact () {
        Contact testContact = new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME);
        testContact.addNotes(MEETING_NOTES_1);

        assertEquals(testContact.getNotes(), MEETING_NOTES_1);
    }

    @Test
    public void testAddNotesToContactWithExistingNotes () {
        Contact testContact = new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME);
        testContact.addNotes(MEETING_NOTES_1);
        testContact.addNotes(MEETING_NOTES_2);

        assertEquals(testContact.getNotes(), MEETING_NOTES_1 + NOTES_DELIMITER + MEETING_NOTES_2);
    }

    @Test
    public void testAddNotesToContactWithNotesInConstructor () {
        Contact testContact = new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME, MEETING_NOTES_1);
        testContact.addNotes(MEETING_NOTES_2);

        assertEquals(testContact.getNotes(), MEETING_NOTES_1 + NOTES_DELIMITER + MEETING_NOTES_2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyNoteToContact () {
        Contact testContact = new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME);
        testContact.addNotes(EMPTY_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithNegativeID () {
        new ContactImpl(ID_NEG, CONTACT_1_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithZeroID () {
        new ContactImpl(ID_ZERO, CONTACT_1_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithEmptyName () {
        new ContactImpl(CONTACT_1_ID, EMPTY_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testNewContactWithNullName() {
        new ContactImpl(CONTACT_1_ID, NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testNewContactWithNullNameAndNotes () {
        new ContactImpl(CONTACT_1_ID, NULL_STRING, NULL_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithEmptyNameAndNullNotes () {
        new ContactImpl(CONTACT_1_ID, EMPTY_STRING, NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testNewContactWithNullNotes () {
        new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME, NULL_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithEmptyNotes () {
        new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME, EMPTY_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullNoteToContact () {
        Contact testContact = new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME);

        testContact.addNotes(NULL_STRING);
    }
}
