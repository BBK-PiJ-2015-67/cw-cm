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

    // some data to work with
    private static final int MEETING_ID = 458;
    private static final int MEETING_ID_NEG = -98;
    private static final int MEETING_ID_ZERO = 0;
    private static final int CONTACT_1_ID = 1;
    private static final int CONTACT_2_ID = 2;
    private static final int CONTACT_3_ID = 3;
    private static final int CONTACT_4_ID = 4;
    private static final String CONTACT_1_NAME = "Han Solo";
    private static final String CONTACT_2_NAME = "Luke Skywalker";
    private static final String CONTACT_3_NAME = "Princess Leia";
    private static final String CONTACT_4_NAME = "Darth Vader";

    private static final int EXPECTED_CONTACT_SET_SIZE = 4;

    // for error checking
    private static final Calendar NULL_CAL = null;
    private static final Set<Contact> NULL_CONTACTS = null;
    private static final Set<Contact> EMPTY_CONTACTS = new HashSet<>();

    private static final int FUTURE_YEAR = 2020;
    private static final int FUTURE_MONTH = 5;
    private static final int FUTURE_DAY = 16;

    private Set<Contact> meetingContacts;
    private Calendar futureDate;
    private Meeting futureMeeting;

    @Before
    public void setUp () {
        meetingContacts = new HashSet<>();
        futureDate = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY);

        meetingContacts.add(new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_2_ID, CONTACT_2_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_3_ID, CONTACT_3_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_4_ID, CONTACT_4_NAME));
    }
    
    @After
    public void tearDown () {
        meetingContacts = null;
        futureMeeting = null;
        futureDate = null;
    }

    @Test
    public void createAFutureMeeting() {
        Meeting mMtg = new FutureMeetingImpl(MEETING_ID, futureDate, meetingContacts);

        assertNotNull(mMtg);
        assertEquals(mMtg.getId(), MEETING_ID);
        assertEquals(mMtg.getDate(), futureDate);
        assertEquals(mMtg.getContacts().size(), EXPECTED_CONTACT_SET_SIZE);
        assertEquals(mMtg.getContacts(), meetingContacts);
    }

    @Test
    public void createAFutureMeetingOfTypeFutureMeeting() {
        FutureMeeting fMtg = new FutureMeetingImpl(MEETING_ID, futureDate, meetingContacts);

        assertNotNull(fMtg);
        assertEquals(fMtg.getId(), MEETING_ID);
        assertEquals(fMtg.getDate(), futureDate);
        assertEquals(fMtg.getContacts().size(), EXPECTED_CONTACT_SET_SIZE);
        assertEquals(fMtg.getContacts(), meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdShouldThrow () {
        futureMeeting = new FutureMeetingImpl(MEETING_ID_NEG, futureDate, meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroIdShouldThrow () {
        futureMeeting = new FutureMeetingImpl(MEETING_ID_ZERO, futureDate, meetingContacts);
    }

    @Test(expected = NullPointerException.class)
    public void nullDateShouldThrow () {
        futureMeeting = new FutureMeetingImpl(MEETING_ID, NULL_CAL, meetingContacts);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsShouldThrow () {
        futureMeeting = new FutureMeetingImpl(MEETING_ID, futureDate, NULL_CONTACTS);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateShouldThrow () {
        futureMeeting = new FutureMeetingImpl(MEETING_ID, NULL_CAL, NULL_CONTACTS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyContactsShouldThrow () {
        futureMeeting = new FutureMeetingImpl(MEETING_ID, futureDate, EMPTY_CONTACTS);
    }
}
