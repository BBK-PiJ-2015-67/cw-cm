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

    private static final int CONTACT_1_ID = 1;
    private static final int CONTACT_2_ID = 2;
    private static final int CONTACT_3_ID = 3;
    private static final int CONTACT_4_ID = 4;
    private static final String CONTACT_1_NAME = "James Spader";
    private static final String CONTACT_2_NAME = "Quentin Tarantino";
    private static final String CONTACT_3_NAME = "Kevin Spacey";
    private static final String CONTACT_4_NAME = "Christopher Nolan";

    private static final int EXPECTED_CONTACT_SET_SIZE = 4;

    private static final int YEAR = 2007;
    private static final int MONTH = 8;
    private static final int DAY = 2;
    private static final int MEETING_ID = 458;
    private static final int MEETING_ID_NEG = -2;
    private static final int MEETING_ID_ZERO = 0;

    private static final String MEETING_NOTES = "Notes";

    private static final String NULL_NOTES = null;
    private static final Calendar NULL_CAL = null;
    private static final Set<Contact> NULL_CONTACTS = null;
    private static final Set<Contact> EMPTY_CONTACTS = new HashSet<>();

    private Meeting meeting;
    private Set<Contact> meetingContacts;
    private Calendar meetingDate;

    @Before
    public void setUp () {
        meetingDate = new GregorianCalendar(YEAR, MONTH, DAY);
        meetingContacts = new HashSet<>();

        meetingContacts.add(new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_2_ID, CONTACT_2_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_3_ID, CONTACT_3_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_4_ID, CONTACT_4_NAME));
    }
    
    @After
    public void tearDown () {
        meeting = null;
        meetingContacts = null;
        meetingDate = null;
    }

    @Test
    public void createAPastMeeting() {
        Meeting mMtg = new PastMeetingImpl(MEETING_ID, meetingDate, meetingContacts, MEETING_NOTES);

        assertNotNull(mMtg);
        assertEquals(mMtg.getId(), MEETING_ID);
        assertEquals(mMtg.getDate(), meetingDate);
        assertEquals(mMtg.getContacts(),meetingContacts);
        assertEquals(mMtg.getContacts().size(), EXPECTED_CONTACT_SET_SIZE);
        assertNotNull(((PastMeeting) mMtg).getNotes());
        assertEquals(((PastMeeting) mMtg).getNotes(), MEETING_NOTES);
    }

    @Test
    public void createAPastMeetingOfTypePastMeeting() {
        PastMeeting pMtg = new PastMeetingImpl(MEETING_ID, meetingDate, meetingContacts, MEETING_NOTES);

        assertNotNull(pMtg);
        assertEquals(pMtg.getId(), MEETING_ID);
        assertEquals(pMtg.getDate(), meetingDate);
        assertEquals(pMtg.getContacts().size(), EXPECTED_CONTACT_SET_SIZE);
        assertEquals(pMtg.getContacts(),meetingContacts);
        assertNotNull(pMtg.getNotes());
        assertEquals(pMtg.getNotes(), MEETING_NOTES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID_NEG, meetingDate, meetingContacts, MEETING_NOTES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroIdShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID_ZERO, meetingDate, meetingContacts, MEETING_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullDateShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, NULL_CAL, meetingContacts, MEETING_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, meetingDate, NULL_CONTACTS, MEETING_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, NULL_CAL, NULL_CONTACTS, MEETING_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullNotesShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, meetingDate, meetingContacts, NULL_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullNotesAndDateShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, NULL_CAL, meetingContacts, NULL_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullNotesAndContactsShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, meetingDate, NULL_CONTACTS, NULL_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateAndNotesShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, NULL_CAL, NULL_CONTACTS, NULL_NOTES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyContactsShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, meetingDate, EMPTY_CONTACTS, MEETING_NOTES);
    }
}
