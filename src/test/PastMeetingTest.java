package test;

import impl.ContactImpl;
import impl.PastMeetingImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.Meeting;
import spec.PastMeeting;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * PastMeeting tests
 *
 * @author lmignot
 */
public class PastMeetingTest {

    private Meeting mtg;
    private int id = 99;
    private Set<Contact> meetingContacts;
    private Calendar date;
    private String mtgNotes;

    @Before
    public void setUp () {
        mtgNotes = "1. We've decided to create a movie starring James and Susan.\n" +
                "2. Quentin has agreed to direct this movie.\n" +
                "3. The movie is to commence filming in the 2nd half of next year.";
        meetingContacts = new HashSet<>();
        date = new GregorianCalendar(2015, 11, 12, 9, 30, 0);

        meetingContacts.add(new ContactImpl(2, "James Spader"));
        meetingContacts.add(new ContactImpl(98, "Susan Sarandon"));
        meetingContacts.add(new ContactImpl(289, "Quentin Tarantino"));
        
        mtg = new PastMeetingImpl(id, date, meetingContacts, mtgNotes);
    }
    
    @After
    public void tearDown () {
        meetingContacts = null;
        mtg = null;
        date = null;
    }

    @Test
    public void createAPastMeeting() {
        Meeting mMtg = new PastMeetingImpl(99,
                new GregorianCalendar(2015, 11, 12, 9, 30, 0), 
                meetingContacts, "Some notes about this other meeting.");

        assertNotNull(mMtg);
        assertEquals(mMtg.getId(),99);
        assertEquals(mMtg.getDate(),new GregorianCalendar(2015, 11, 12, 9, 30, 0));
        assertEquals(mMtg.getContacts().size(),3);
        assertEquals(mMtg.getContacts(),meetingContacts);
        assertNotNull(((PastMeeting) mMtg).getNotes());
        assertEquals(((PastMeeting) mMtg).getNotes(),"Some notes about this other meeting.");
    }

    @Test
    public void createAPastMeetingOfTypePastMeeting() {
        PastMeeting pMtg = new PastMeetingImpl(99,
                new GregorianCalendar(2015, 11, 12, 9, 30, 0),
                meetingContacts, "");

        assertNotNull(pMtg);
        assertEquals(pMtg.getId(),99);
        assertEquals(pMtg.getDate(),new GregorianCalendar(2015, 11, 12, 9, 30, 0));
        assertEquals(pMtg.getContacts().size(),3);
        assertEquals(pMtg.getContacts(),meetingContacts);
        assertNotNull(pMtg.getNotes());
        assertEquals(pMtg.getNotes(),"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdShouldThrow () {
        mtg = new PastMeetingImpl(-2, date, meetingContacts, "Notes");
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroIdShouldThrow () {
        mtg = new PastMeetingImpl(0, date, meetingContacts, "Notes");
    }

    @Test(expected = NullPointerException.class)
    public void nullDateShouldThrow () {
        mtg = new PastMeetingImpl(id, null, meetingContacts, "Notes");
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsShouldThrow () {
        mtg = new PastMeetingImpl(id, date, null, "Notes");
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateShouldThrow () {
        mtg = new PastMeetingImpl(id, null, null, "Notes");
    }

    @Test(expected = NullPointerException.class)
    public void nullNotesShouldThrow () {
        mtg = new PastMeetingImpl(id, date, meetingContacts, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullNotesAndDateShouldThrow () {
        mtg = new PastMeetingImpl(id, null, meetingContacts, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullNotesAndContactsShouldThrow () {
        mtg = new PastMeetingImpl(id, date, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateAndNotesShouldThrow () {
        mtg = new PastMeetingImpl(id, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyContactsShouldThrow () {
        mtg = new PastMeetingImpl(id, date, new HashSet<>(), "Notes");
    }
}
