package test;

import impl.ContactImpl;
import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.*;

import java.util.*;

import static org.junit.Assert.*;
import static test.TestCommon.*;

/**
 * ContactManager tests
 * Split up the tests as the main test class was getting quite large
 *
 * This class tests the Meeting methods in ContactManager
 * @author lmignot
 */
public class ContactManagerMeetingsTest {

    private ContactManager emptyCM;
    private ContactManager meetingsCM;
    private Set<Contact> meetingContacts;

    private Calendar futureDate;
    private Calendar pastDate;
    private Calendar currentDate;

    @Before
    public void setUp() {
        deleteDataFile();
        emptyCM = new ContactManagerImpl();
        meetingsCM = new ContactManagerImpl();
        addFourContactsToCm(meetingsCM);

        meetingContacts = meetingsCM.getContacts(CONTACT_1_ID, CONTACT_3_ID, CONTACT_4_ID);

        currentDate = new GregorianCalendar();
        futureDate = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY);
        pastDate = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY);
    }

    @After
    public void tearDown() {
        deleteDataFile();

        emptyCM = null;
        meetingsCM = null;
        meetingContacts = null;
        currentDate = null;
        futureDate = null;
        pastDate = null;
    }

    /* =================== MEETINGS =================== */

    @Test
    public void testGetExistingMeeting () {
        int id = meetingsCM.addFutureMeeting(meetingContacts, futureDate);
        Meeting mtg = meetingsCM.getMeeting(id);

        assertNotNull(mtg);
        assertEquals(mtg.getId(), id);
        assertEquals(mtg.getDate(), futureDate);
        assertEquals(mtg.getContacts(), meetingContacts);
    }

    /* =================== FUTURE MEETINGS =================== */

    @Test
    public void testAddFutureMeeting () {
        int futureMeetingId = meetingsCM.addFutureMeeting(meetingContacts, futureDate);
        currentDate.add(Calendar.SECOND, THIRTY_SECONDS);
        int futureMeetingId2 = meetingsCM.addFutureMeeting(meetingContacts, currentDate);

        assertEquals(futureMeetingId, FIRST_MEETING_ID);
        assertEquals(futureMeetingId2, SECOND_MEETING_ID);
    }

    @Test
    public void testGetFutureMeeting () {
        int futureMeetingId = meetingsCM.addFutureMeeting(meetingContacts, futureDate);

        Meeting meeting = meetingsCM.getFutureMeeting(futureMeetingId);

        assertNotNull(meeting);
        assertFalse(meeting instanceof PastMeeting);
        assertEquals(meeting.getId(), futureMeetingId);
        assertEquals(meeting.getDate(), futureDate);
        assertEquals(meeting.getContacts(), meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFutureMeetingWithPastMeetingId () {
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate, MEETING_NOTES_1);
        meetingsCM.getFutureMeeting(FIRST_MEETING_ID);
    }

    @Test(expected = NullPointerException.class)
    public void testAddFutureMeetingWithNullContacts () {
        meetingsCM.addFutureMeeting(NULL_CONTACTS, futureDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFutureMeetingWithEmptyContacts () {
        meetingsCM.addFutureMeeting(EMPTY_CONTACTS, futureDate);
    }

    @Test(expected = NullPointerException.class)
    public void testAddFutureMeetingWithNullDate () {
        meetingsCM.addFutureMeeting(meetingContacts, NULL_CAL);
    }

    @Test(expected = NullPointerException.class)
    public void testAddFutureMeetingWithNullDateAndContacts () {
        meetingsCM.addFutureMeeting(NULL_CONTACTS, NULL_CAL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFutureMeetingWithPastDate () {
        meetingsCM.addFutureMeeting(meetingContacts, pastDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFutureMeetingWithCurrentDate () {
        meetingsCM.addFutureMeeting(meetingContacts, currentDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFutureMeetingWithInvalidContacts () {
        emptyCM.addFutureMeeting(meetingContacts, pastDate);
    }

    /* =================== PAST MEETINGS =================== */

    @Test
    public void testAddPastMeeting () {
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate, MEETING_NOTES_1);
        PastMeeting pMtg = meetingsCM.getPastMeeting(FIRST_MEETING_ID);

        assertNotNull(pMtg);
        assertEquals(pMtg.getId(), FIRST_MEETING_ID);
        assertEquals(pMtg.getContacts(), meetingContacts);
        assertEquals(pMtg.getDate(), pastDate);
        assertEquals(pMtg.getNotes(), MEETING_NOTES_1);
    }

    @Test
    public void testGetPastMeeting () {
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate, MEETING_NOTES_1);

        PastMeeting pMtg = meetingsCM.getPastMeeting(FIRST_MEETING_ID);

        assertNotNull(pMtg);
        assertFalse(pMtg instanceof FutureMeeting);
        assertEquals(pMtg.getId(), FIRST_MEETING_ID);
        assertEquals(pMtg.getDate(), pastDate);
        assertEquals(pMtg.getContacts(), meetingContacts);
        assertEquals(pMtg.getNotes(), MEETING_NOTES_1);
    }

    @Test
    public void testAddPastMeetingWithCurrentDateMinusFiveMillis () {
        currentDate.add(Calendar.MILLISECOND, MINUS_FIVE_MILLISECONDS);
        meetingsCM.addNewPastMeeting(meetingContacts, currentDate, MEETING_NOTES_1);
        PastMeeting pMtg = meetingsCM.getPastMeeting(FIRST_MEETING_ID);

        assertNotNull(pMtg);
        assertEquals(pMtg.getId(), FIRST_MEETING_ID);
        assertEquals(pMtg.getDate(), currentDate);
        assertEquals(pMtg.getNotes(), MEETING_NOTES_1);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPastMeetingWithFutureMeetingId () {
        meetingsCM.addFutureMeeting(meetingContacts, futureDate);
        meetingsCM.getPastMeeting(FIRST_MEETING_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPastMeetingWithFutureDate () {
        meetingsCM.addNewPastMeeting(meetingContacts, futureDate, MEETING_NOTES_1);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPastMeetingWithNullContacts () {
        meetingsCM.addNewPastMeeting(NULL_CONTACTS, pastDate, MEETING_NOTES_1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPastMeetingWithEmptyContacts () {
        meetingsCM.addNewPastMeeting(EMPTY_CONTACTS, pastDate, MEETING_NOTES_1);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPastMeetingWithNullDate () {
        meetingsCM.addNewPastMeeting(meetingContacts, NULL_CAL, MEETING_NOTES_1);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPastMeetingWithNullNotes () {
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate, NULL_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPastMeetingWithEmptyNotes () {
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate, EMPTY_STRING);
    }

    /* =================== MEETING LISTS =================== */

    @Test
    public void testGetEmptyFutureMeetingListIsEmpty() {
        Contact testContact = meetingsCM.getContacts(CONTACT_1_ID).stream().findFirst().get();
        List<Meeting> futureMeetingList = meetingsCM.getFutureMeetingList(testContact);

        assertNotNull(futureMeetingList);
        assertEquals(futureMeetingList.size(), EMPTY_SIZE);
        assertTrue(futureMeetingList.isEmpty());
    }

    @Test
    public void testGetFutureMeetingListIsNotEmptyAndSorted() {
        Contact testContact = meetingsCM.getContacts(CONTACT_1_ID).stream().findFirst().get();

        Calendar futureDate1 = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY, HOUR_11, MINUTE_20);
        Calendar futureDate2 = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY, HOUR_9, MINUTE_15);
        Calendar futureDate3 = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY, HOUR_14, MINUTE_35);

        meetingsCM.addFutureMeeting(meetingContacts, futureDate1); // ID: 1
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate, MEETING_NOTES_1); // ID: 2
        meetingsCM.addFutureMeeting(meetingContacts, futureDate2); // ID: 3
        meetingsCM.addFutureMeeting(meetingContacts, futureDate3); // ID: 4
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate, MEETING_NOTES_2); // ID: 5

        List<Meeting> futureMeetingList = meetingsCM.getFutureMeetingList(testContact);

        assertNotNull(futureMeetingList);
        assertEquals(futureMeetingList.size(), 3);
        assertFalse(futureMeetingList.isEmpty());

        Meeting mtg1 = futureMeetingList.get(0);
        Meeting mtg2 = futureMeetingList.get(1);
        Meeting mtg3 = futureMeetingList.get(2);

        assertEquals(mtg1.getId(), 3);
        assertEquals(mtg2.getId(), 1);
        assertEquals(mtg3.getId(), 4);

        assertEquals(mtg1.getDate(), futureDate2);
        assertEquals(mtg2.getDate(), futureDate1);
        assertEquals(mtg3.getDate(), futureDate3);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFutureMeetingListWithNullContactShouldThrow () {
        meetingsCM.getFutureMeetingList(NULL_CONTACT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFutureMeetingListWithInvalidContactShouldThrow () {
        meetingsCM.getFutureMeetingList(new ContactImpl(ILLEGAL_ID_3, NON_EXISTENT_CONTACT_NAME));
    }

    @Test
    public void testGetMeetingListOnIsEmpty() {
        List<Meeting> meetingList = meetingsCM.getMeetingListOn(
            new GregorianCalendar(FUTURE_YEAR,FUTURE_MONTH, FUTURE_DAY)
        );

        assertNotNull(meetingList);
        assertEquals(meetingList.size(), EMPTY_SIZE);
        assertTrue(meetingList.isEmpty());
    }

    @Test
    public void testGetMeetingListOnFuture() {
        Calendar futureDate1 = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY, HOUR_11, MINUTE_20);
        Calendar futureDate2 = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY, HOUR_9, MINUTE_15);
        Calendar futureDate3 = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY, HOUR_14, MINUTE_35);

        meetingsCM.addFutureMeeting(meetingContacts, futureDate1); // ID: 1
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate, MEETING_NOTES_1); // ID: 2
        meetingsCM.addFutureMeeting(meetingContacts, futureDate2); // ID: 3
        meetingsCM.addFutureMeeting(meetingContacts, futureDate3); // ID: 4
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate, MEETING_NOTES_2); // ID: 5

        List<Meeting> meetingList = meetingsCM.getMeetingListOn(futureDate);

        assertNotNull(meetingList);
        assertEquals(meetingList.size(), 3);
        assertFalse(meetingList.isEmpty());

        Meeting mtg1 = meetingList.get(0);
        Meeting mtg2 = meetingList.get(1);
        Meeting mtg3 = meetingList.get(2);

        assertEquals(mtg1.getId(), 3);
        assertEquals(mtg2.getId(), 1);
        assertEquals(mtg3.getId(), 4);

        assertEquals(mtg1.getDate(), futureDate2);
        assertEquals(mtg2.getDate(), futureDate1);
        assertEquals(mtg3.getDate(), futureDate3);
    }

    @Test
    public void testGetMeetingListOnPast() {
        Calendar pastDate1 = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY, HOUR_11, MINUTE_15);
        Calendar pastDate2 = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY, HOUR_9, MINUTE_20);
        Calendar pastDate3 = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY, HOUR_14, MINUTE_35);

        meetingsCM.addFutureMeeting(meetingContacts, futureDate); // ID: 1
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate1, "Notes"); // ID: 2
        meetingsCM.addFutureMeeting(meetingContacts, futureDate); // ID: 3
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate2, "Notes"); // ID: 4
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate3, "Notes"); // ID: 5

        List<Meeting> meetingList = meetingsCM.getMeetingListOn(new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY));

        assertNotNull(meetingList);
        assertFalse(meetingList.isEmpty());
        assertEquals(meetingList.size(), 3);

        Meeting mtg1 = meetingList.get(0);
        Meeting mtg2 = meetingList.get(1);
        Meeting mtg3 = meetingList.get(2);

        assertEquals(mtg1.getId(), 4);
        assertEquals(mtg2.getId(), 2);
        assertEquals(mtg3.getId(), 5);

        assertEquals(mtg1.getDate(), pastDate2);
        assertEquals(mtg2.getDate(), pastDate1);
        assertEquals(mtg3.getDate(), pastDate3);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMeetingListOnShouldThrowForNullDate() {
        meetingsCM.getMeetingListOn(NULL_CAL);
    }

    @Test
    public void testGetEmptyPastMeetingListForIsEmpty() {
        Set<Contact> contactSet = meetingsCM.getContacts(CONTACT_1_ID);
        Contact testContact = contactSet.stream().findFirst().get();

        List<PastMeeting> pastMeetingList = meetingsCM.getPastMeetingListFor(testContact);

        assertNotNull(pastMeetingList);
        assertEquals(pastMeetingList.size(), EMPTY_SIZE);
        assertTrue(pastMeetingList.isEmpty());
    }

    @Test
    public void testGetPastMeetingListForIsNotEmptyAndSorted() {
        Contact testContact = meetingsCM.getContacts(CONTACT_1_ID).stream().findFirst().get();

        Calendar pastDate1 = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY, HOUR_11, MINUTE_20);
        Calendar pastDate2 = new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY, HOUR_14, MINUTE_35);
        Calendar pastDate3 = new GregorianCalendar(PAST_YEAR_2, PAST_MONTH, PAST_DAY);
        Calendar pastDate4 = new GregorianCalendar(PAST_YEAR_3, PAST_MONTH, PAST_DAY);

        meetingsCM.addFutureMeeting(meetingContacts, futureDate); // ID: 1
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate1, MEETING_NOTES_1); // ID: 2
        meetingsCM.addFutureMeeting(meetingContacts, futureDate); // ID: 3
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate2, MEETING_NOTES_2); // ID: 4
        meetingsCM.addFutureMeeting(meetingContacts, futureDate); // ID: 5
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate4, MEETING_NOTES_3); // ID: 6
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate3, MEETING_NOTES_4); // ID: 7

        List<PastMeeting> pastMeetings = meetingsCM.getPastMeetingListFor(testContact);

        assertNotNull(pastMeetings);
        assertEquals(pastMeetings.size(), 4);
        assertFalse(pastMeetings.isEmpty());

        // expected meeting ID order 2, 4, 7, 6
        Meeting mtg1 = pastMeetings.get(0);
        Meeting mtg2 = pastMeetings.get(1);
        Meeting mtg3 = pastMeetings.get(2);
        Meeting mtg4 = pastMeetings.get(3);

        assertEquals(mtg1.getId(), 2);
        assertEquals(mtg2.getId(), 4);
        assertEquals(mtg3.getId(), 7);
        assertEquals(mtg4.getId(), 6);

        assertEquals(mtg1.getDate(), pastDate1);
        assertEquals(mtg2.getDate(), pastDate2);
        assertEquals(mtg3.getDate(), pastDate3);
        assertEquals(mtg4.getDate(), pastDate4);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPastMeetingListForWithNullContactShouldThrow () {
        meetingsCM.getPastMeetingListFor(NULL_CONTACT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPastMeetingListForWithInvalidContactShouldThrow () {
        meetingsCM.getPastMeetingListFor(new ContactImpl(ILLEGAL_ID_1, NON_EXISTENT_CONTACT_NAME));
    }

    @Test
    public void testAddMeetingNotes () {
        Calendar futureTime = new GregorianCalendar();
        futureTime.add(Calendar.MILLISECOND, ONE_MILLISECOND);
        meetingsCM.addFutureMeeting(meetingContacts, futureTime);

        // pause for 5 milliseconds to ensure the new Future meeting
        // is now in the past
        try {
            Thread.sleep(FIVE_MILLISECONDS);
        } catch (InterruptedException iEx) {
            iEx.printStackTrace();
        }

        meetingsCM.addMeetingNotes(FIRST_MEETING_ID, MEETING_NOTES_1);
        assertEquals(meetingsCM.getPastMeeting(FIRST_MEETING_ID).getNotes(), MEETING_NOTES_1);
    }

    @Test
    public void testAppendMeetingNotes () {
        meetingsCM.addNewPastMeeting(meetingContacts, pastDate, MEETING_NOTES_1);
        meetingsCM.addMeetingNotes(FIRST_MEETING_ID, MEETING_NOTES_2);
        meetingsCM.addMeetingNotes(FIRST_MEETING_ID, MEETING_NOTES_3);

        assertEquals(meetingsCM.getPastMeeting(FIRST_MEETING_ID).getNotes(),
                MEETING_NOTES_1 + NOTES_DELIMITER + MEETING_NOTES_2 + NOTES_DELIMITER + MEETING_NOTES_3);
    }

    @Test(expected = NullPointerException.class)
    public void testAddMeetingNotesWithNullNotes () {
        meetingsCM.addMeetingNotes(FIRST_MEETING_ID, NULL_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMeetingNotesWithInvalidId () {
        meetingsCM.addMeetingNotes(ILLEGAL_ID_3, MEETING_NOTES_1);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddMeetingNotesToFutureMeeting () {
        meetingsCM.addFutureMeeting(meetingContacts, futureDate);
        meetingsCM.addMeetingNotes(FIRST_MEETING_ID, MEETING_NOTES_1);
    }
}
