package test;

import impl.ContactImpl;
import impl.FutureMeetingImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.FutureMeeting;
import spec.Meeting;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * FutureMeeting tests
 *
 * @author lmignot
 */
public class FutureMeetingTest {

    private Meeting mtg;
    private int id = 72;
    private Set<Contact> meetingContacts;
    private Calendar date;

    @Before
    public void setUp () {
        meetingContacts = new HashSet<>();
        date = new GregorianCalendar(2020, 2, 29, 6, 59, 0);

        meetingContacts.add(new ContactImpl(23, "Jim Harrison"));
        meetingContacts.add(new ContactImpl(33, "Van Wrinkle"));
        meetingContacts.add(new ContactImpl(99, "Susan Doubtfire"));
        meetingContacts.add(new ContactImpl(2, "Harry Smith"));
        
        mtg = new FutureMeetingImpl(id, date, meetingContacts);
    }
    
    @After
    public void tearDown () {
        meetingContacts = null;
        mtg = null;
        date = null;
    }

    @Test
    public void createAFutureMeeting() {
        Meeting mMtg = new FutureMeetingImpl(48, new GregorianCalendar(2020, 2, 29, 6, 59, 0), meetingContacts);

        assertNotNull(mMtg);
        assertEquals(mMtg.getId(),48);
        assertEquals(mMtg.getDate(),new GregorianCalendar(2020, 2, 29, 6, 59, 0));
        assertEquals(mMtg.getContacts().size(),4);
        assertEquals(mMtg.getContacts(),meetingContacts);
    }

    @Test
    public void createAFutureMeetingOfTypeFutureMeeting() {
        FutureMeeting fMtg = new FutureMeetingImpl(48, new GregorianCalendar(2020, 2, 29, 6, 59, 0), meetingContacts);

        assertNotNull(fMtg);
        assertEquals(fMtg.getId(),48);
        assertEquals(fMtg.getDate(),new GregorianCalendar(2020, 2, 29, 6, 59, 0));
        assertEquals(fMtg.getContacts().size(),4);
        assertEquals(fMtg.getContacts(),meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdShouldThrow () {
        mtg = new FutureMeetingImpl(-2, date, meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroIdShouldThrow () {
        mtg = new FutureMeetingImpl(0, date, meetingContacts);
    }

    @Test(expected = NullPointerException.class)
    public void nullDateShouldThrow () {
        mtg = new FutureMeetingImpl(id, null, meetingContacts);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsShouldThrow () {
        mtg = new FutureMeetingImpl(id, date, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateShouldThrow () {
        mtg = new FutureMeetingImpl(id, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyContactsShouldThrow () {
        mtg = new FutureMeetingImpl(id, date, new HashSet<>());
    }
}
