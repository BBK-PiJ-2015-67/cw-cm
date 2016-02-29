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
import static test.TestCommon.*;

/**
 * PastMeeting tests
 *
 * @author lmignot
 */
public class PastMeetingTest {

    private Meeting meeting;
    private Set<Contact> meetingContacts;
    private Calendar meetingDate;

    @Before
    public void setUp () {
        meetingDate = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY);
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
        Meeting mMtg = new PastMeetingImpl(MEETING_ID, meetingDate, meetingContacts, NOTES_1);

        assertNotNull(mMtg);
        assertEquals(mMtg.getId(), MEETING_ID);
        assertEquals(mMtg.getDate(), meetingDate);
        assertEquals(mMtg.getContacts(),meetingContacts);
        assertEquals(mMtg.getContacts().size(), EXPECTED_CONTACT_SET_SIZE);
        assertNotNull(((PastMeeting) mMtg).getNotes());
        assertEquals(((PastMeeting) mMtg).getNotes(), NOTES_1);
    }

    @Test
    public void createAPastMeetingOfTypePastMeeting() {
        PastMeeting pMtg = new PastMeetingImpl(MEETING_ID, meetingDate, meetingContacts, NOTES_1);

        assertNotNull(pMtg);
        assertEquals(pMtg.getId(), MEETING_ID);
        assertEquals(pMtg.getDate(), meetingDate);
        assertEquals(pMtg.getContacts().size(), EXPECTED_CONTACT_SET_SIZE);
        assertEquals(pMtg.getContacts(),meetingContacts);
        assertNotNull(pMtg.getNotes());
        assertEquals(pMtg.getNotes(), NOTES_1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdShouldThrow () {
        meeting = new PastMeetingImpl(ID_NEG, meetingDate, meetingContacts, NOTES_1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroIdShouldThrow () {
        meeting = new PastMeetingImpl(ID_ZERO, meetingDate, meetingContacts, NOTES_1);
    }

    @Test(expected = NullPointerException.class)
    public void nullDateShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, NULL_CAL, meetingContacts, NOTES_1);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, meetingDate, NULL_CONTACTS, NOTES_1);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateShouldThrow () {
        meeting = new PastMeetingImpl(MEETING_ID, NULL_CAL, NULL_CONTACTS, NOTES_1);
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
        meeting = new PastMeetingImpl(MEETING_ID, meetingDate, EMPTY_CONTACTS, NOTES_1);
    }
}
