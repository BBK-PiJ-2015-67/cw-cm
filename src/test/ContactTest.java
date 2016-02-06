package test;

import org.junit.Before;
import org.junit.Test;
import spec.Contact;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing a Contact
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

        assertThat(testContact.getId()).isEqualTo(stubID);
        assertThat(testContact.getName()).isEqualTo(stubName);
        assertThat(testContact.getNotes()).isEqualTo(notesA);
    }

    @Test
    public void testNewContactWithNoNotes () {
        Contact testContact = new ContactImpl(stubID, stubName);

        assertThat(testContact.getId()).isEqualTo(stubID);
        assertThat(testContact.getName()).isEqualTo(stubName);
    }

    @Test
    public void testAddNotesToContact () {
        Contact testContact = new ContactImpl(stubID, stubName);

        testContact.addNotes(notesA);

        assertThat(testContact.getNotes()).isEqualTo(notesA);
    }

    @Test
    public void testAddNotesToContactWithExistingNotes () {
        Contact testContact = new ContactImpl(stubID, stubName);

        testContact.addNotes(notesA);
        testContact.addNotes(notesB);

        assertThat(testContact.getNotes()).isEqualTo(notesA + notesB);
    }

    @Test
    public void testAddNotesToContactWithNotesInConstructor () {
        Contact testContact = new ContactImpl(stubID, stubName, notesA);

        testContact.addNotes(notesB);

        assertThat(testContact.getNotes()).isEqualTo(notesA + notesB);
    }
}
