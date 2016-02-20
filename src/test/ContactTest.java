package test;

import impl.ContactImpl;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;

import static org.junit.Assert.assertEquals;

/**
 * Testing a Contact
 *
 * @author lmignot
 */
public class ContactTest {

    private int stubID;
    private String stubName;
    private String notesA;
    private String notesB;

    @Before
    public void setUp () {
        stubID = 32;
        stubName = "Carl Lewis";
        notesA = "This is Carl, Carl is smart.";
        notesB = "Carl doesn't make fun of noobs, Carl helps them out.";
    }

    @Test
    public void testNewContactWithNotes () {
        Contact testContact = new ContactImpl(stubID, stubName, notesA);

        assertEquals(testContact.getId(),stubID);
        assertEquals(testContact.getName(),stubName);
        assertEquals(testContact.getNotes(),notesA);
    }

    @Test
    public void testNewContactWithNoNotes () {
        Contact testContact = new ContactImpl(stubID, stubName);

        assertEquals(testContact.getId(),stubID);
        assertEquals(testContact.getName(),stubName);
    }

    @Test
    public void testAddNotesToContact () {
        Contact testContact = new ContactImpl(stubID, stubName);

        testContact.addNotes(notesA);

        assertEquals(testContact.getNotes(),notesA);
    }

    @Test
    public void testAddEmptyNoteToContact () {
        Contact testContact = new ContactImpl(stubID, stubName);

        testContact.addNotes("");

        assertEquals(testContact.getNotes(),"");
    }

    @Test
    public void testAddNotesToContactWithExistingNotes () {
        Contact testContact = new ContactImpl(stubID, stubName);

        testContact.addNotes(notesA);
        testContact.addNotes(notesB);

        assertEquals(testContact.getNotes(),notesA + notesB);
    }

    @Test
    public void testAddNotesToContactWithNotesInConstructor () {
        Contact testContact = new ContactImpl(stubID, stubName, notesA);

        testContact.addNotes(notesB);

        assertEquals(testContact.getNotes(),notesA + notesB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithNegativeID () {
        new ContactImpl(-2, stubName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithZeroID () {
        new ContactImpl(0, stubName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithEmptyName () {
        new ContactImpl(1, "");
    }

    @Test(expected = NullPointerException.class)
    public void testNewContactWithNullName() {
        new ContactImpl(1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNewContactWithNullNameAndNotes () {
        new ContactImpl(1, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewContactWithEmptyNameAndNotes () {
        new ContactImpl(1, "", null);
    }

    @Test(expected = NullPointerException.class)
    public void testNewContactWithNullNotes () {
        new ContactImpl(1, stubName, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullNoteToContact () {
        Contact testContact = new ContactImpl(stubID, stubName);

        testContact.addNotes(null);
    }
}
