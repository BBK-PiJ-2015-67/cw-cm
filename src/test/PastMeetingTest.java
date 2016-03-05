package test;

import impl.PastMeetingImpl;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.Meeting;
import spec.PastMeeting;

import java.util.Calendar;
import java.util.GregorianCalendar;
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

    private Set<Contact> meetingContacts;
    private Calendar meetingDate;

    @Before
    public void setUp () {
        meetingDate = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY);
        meetingContacts = getMeetingContacts();
    }

    @Test
    public void createAPastMeeting() {
        Meeting mMtg = new PastMeetingImpl(MEETING_ID, meetingDate, meetingContacts, MEETING_NOTES);

        assertNotNull(mMtg);
        assertEquals(mMtg.getId(), MEETING_ID);
        assertEquals(mMtg.getDate(), meetingDate);
        assertEquals(mMtg.getContacts(),meetingContacts);
        assertEquals(mMtg.getContacts().size(), EXPECTED_NUM_CONTACTS_4);
        assertNotNull(((PastMeeting) mMtg).getNotes());
        assertEquals(((PastMeeting) mMtg).getNotes(), MEETING_NOTES);
    }

    @Test
    public void createAPastMeetingOfTypePastMeeting() {
        PastMeeting pMtg = new PastMeetingImpl(MEETING_ID, meetingDate, meetingContacts, MEETING_NOTES);

        assertNotNull(pMtg);
        assertEquals(pMtg.getId(), MEETING_ID);
        assertEquals(pMtg.getDate(), meetingDate);
        assertEquals(pMtg.getContacts().size(), EXPECTED_NUM_CONTACTS_4);
        assertEquals(pMtg.getContacts(),meetingContacts);
        assertNotNull(pMtg.getNotes());
        assertEquals(pMtg.getNotes(), MEETING_NOTES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdShouldThrow () {
        new PastMeetingImpl(ID_NEG, meetingDate, meetingContacts, MEETING_NOTES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroIdShouldThrow () {
        new PastMeetingImpl(ID_ZERO, meetingDate, meetingContacts, MEETING_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullDateShouldThrow () {
        new PastMeetingImpl(MEETING_ID, NULL_CAL, meetingContacts, MEETING_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsShouldThrow () {
        new PastMeetingImpl(MEETING_ID, meetingDate, NULL_CONTACTS, MEETING_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateShouldThrow () {
        new PastMeetingImpl(MEETING_ID, NULL_CAL, NULL_CONTACTS, MEETING_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void nullNotesShouldThrow () {
        new PastMeetingImpl(MEETING_ID, meetingDate, meetingContacts, NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void nullNotesAndDateShouldThrow () {
        new PastMeetingImpl(MEETING_ID, NULL_CAL, meetingContacts, NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void nullNotesAndContactsShouldThrow () {
        new PastMeetingImpl(MEETING_ID, meetingDate, NULL_CONTACTS, NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateAndNotesShouldThrow () {
        new PastMeetingImpl(MEETING_ID, NULL_CAL, NULL_CONTACTS, NULL_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyContactsShouldThrow () {
        new PastMeetingImpl(MEETING_ID, meetingDate, EMPTY_CONTACTS, MEETING_NOTES);
    }
}
