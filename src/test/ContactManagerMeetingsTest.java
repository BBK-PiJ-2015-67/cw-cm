package test;

import impl.ContactImpl;
import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * ContactManager tests
 * Split up the tests as the main test class was getting quite large
 *
 * This class tests the Meeting methods in ContactManager
 * @author lmignot
 */
public class ContactManagerMeetingsTest {

    private ContactManager cMgr;
    private ContactManager cMgrHasContacts;
    private Set<Contact> meetingContacts;

    private Calendar futureDate;
    private Calendar pastDate;
    private Calendar now;

    private String testMeetingNotes;

    @Before
    public void setUp() {
        // delete any data file if present before running tests
        TestUtils.deleteCMDataFile();

        cMgr = new ContactManagerImpl();
        cMgrHasContacts = new ContactManagerImpl();

        now = new GregorianCalendar();
        futureDate = new GregorianCalendar(2017, 3, 27);
        pastDate = new GregorianCalendar(2014, 4, 20);

        testMeetingNotes = "These are some notes that we took in our past meeting";

        cMgrHasContacts.addNewContact("Aaron Kamen", "Come in... get it?");
        cMgrHasContacts.addNewContact("Xenia Garand", "This one's xenophobic");
        cMgrHasContacts.addNewContact("Sherlene Westrich", "From the west");
        cMgrHasContacts.addNewContact("Emmaline Cupit", "Cupid's daughter");
        cMgrHasContacts.addNewContact("Kendra Kinghorn", "Said to hold the secret to the legendary horn");
        cMgrHasContacts.addNewContact("Ellis Pollak", "Probably a genius come to think of it");
        cMgrHasContacts.addNewContact("Carrol Sin", "Christmas is his favourite time");
        cMgrHasContacts.addNewContact("Norman Wiedemann", "AKA the weed slayer");
        cMgrHasContacts.addNewContact("Efren Apodaca", "There's a pharmacist in his future...");
        cMgrHasContacts.addNewContact("Errol Flynn", "The ultimate swashbuckler");

        meetingContacts = cMgrHasContacts.getContacts(1,2,5,6,9);
    }

    @After
    public void tearDown() {
        // delete any data file if present after running tests
        TestUtils.deleteCMDataFile();

        cMgr = null;
        cMgrHasContacts = null;
        meetingContacts = null;
        now = null;
        futureDate = null;
        pastDate = null;
    }

    /* =================== MEETINGS =================== */

    @Test
    public void testGetMeeting () {
        int id = cMgrHasContacts.addFutureMeeting(meetingContacts, futureDate);
        Meeting mtg = cMgrHasContacts.getMeeting(id);

        assertNotNull(mtg);
        assertEquals(mtg.getId(),id);
        assertEquals(mtg.getDate(),futureDate);
        assertEquals(mtg.getContacts(),meetingContacts);
        assertTrue(mtg instanceof Meeting);
    }

    /* =================== FUTURE MEETINGS =================== */

    @Test
    public void testAddFutureMeeting () {
        int futureMeetingId = cMgrHasContacts.addFutureMeeting(meetingContacts, futureDate);
        Calendar veryNearFuture = new GregorianCalendar();
        veryNearFuture.add(Calendar.SECOND, 30);
        int futureMeetingId2 = cMgrHasContacts.addFutureMeeting(meetingContacts,veryNearFuture);

        assertEquals(futureMeetingId,1);
        assertEquals(futureMeetingId2,2);
    }

    @Test
    public void testGetFutureMeeting () {
        int futureMeetingId = cMgrHasContacts.addFutureMeeting(meetingContacts, futureDate);

        FutureMeeting fMtg = cMgrHasContacts.getFutureMeeting(futureMeetingId);
        Meeting mtg = cMgrHasContacts.getFutureMeeting(futureMeetingId);

        assertNotNull(mtg);
        assertTrue(mtg instanceof FutureMeeting);
        assertFalse(mtg instanceof PastMeeting);

        assertNotNull(fMtg);
        assertFalse(fMtg instanceof PastMeeting);
        assertEquals(fMtg.getId(),futureMeetingId);
        assertEquals(fMtg.getDate(),futureDate);
        assertEquals(fMtg.getContacts(),meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFutureMeetingWithPastMeetingId () {
        cMgrHasContacts.addNewPastMeeting(meetingContacts, pastDate, testMeetingNotes);

        cMgrHasContacts.getFutureMeeting(1);
    }

    @Test(expected = NullPointerException.class)
    public void testAddFutureMeetingWithNullContacts () {
        cMgrHasContacts.addFutureMeeting(null, futureDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFutureMeetingWithEmptyContacts () {
        cMgrHasContacts.addFutureMeeting(new HashSet<>(), futureDate);
    }

    @Test(expected = NullPointerException.class)
    public void testAddFutureMeetingWithNullDate () {
        cMgrHasContacts.addFutureMeeting(meetingContacts, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddFutureMeetingWithNullDateAndContacts () {
        cMgrHasContacts.addFutureMeeting(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFutureMeetingWithPastDate () {
        cMgrHasContacts.addFutureMeeting(meetingContacts, pastDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFutureMeetingWithCurrentDate () {
        cMgrHasContacts.addFutureMeeting(meetingContacts, now);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFutureMeetingWithInvalidContacts () {
        cMgr.addFutureMeeting(meetingContacts, pastDate);
    }

    /* =================== PAST MEETINGS =================== */
    @Test
    public void testAddPastMeeting () {
        cMgrHasContacts.addNewPastMeeting(meetingContacts, pastDate, testMeetingNotes);
        PastMeeting pMtg = cMgrHasContacts.getPastMeeting(1);

        assertNotNull(pMtg);
        assertEquals(pMtg.getId(), 1);
        assertEquals(pMtg.getContacts(), meetingContacts);
        assertEquals(pMtg.getDate(), pastDate);
        assertEquals(pMtg.getNotes(), testMeetingNotes);
    }

    @Test
    public void testGetPastMeeting () {
        cMgrHasContacts.addNewPastMeeting(meetingContacts, pastDate, testMeetingNotes);

        PastMeeting pMtg = cMgrHasContacts.getPastMeeting(1);
        Meeting mtg = cMgrHasContacts.getPastMeeting(1);

        assertNotNull(mtg);

        assertTrue(mtg instanceof PastMeeting);
        assertFalse(mtg instanceof FutureMeeting);

        assertNotNull(pMtg);
        assertFalse(pMtg instanceof FutureMeeting);
        assertEquals(pMtg.getId(),1);
        assertEquals(pMtg.getDate(),pastDate);
        assertEquals(pMtg.getContacts(),meetingContacts);
        assertEquals(pMtg.getNotes(),testMeetingNotes);
    }

    @Test
    public void testAddPastMeetingWithCurrentDate () {
        cMgrHasContacts.addNewPastMeeting(meetingContacts, now, testMeetingNotes);
        PastMeeting pMtg = cMgrHasContacts.getPastMeeting(1);

        assertNotNull(pMtg);
        assertEquals(pMtg.getId(), 1);
        assertEquals(pMtg.getDate(), now);
        assertEquals(pMtg.getNotes(), testMeetingNotes);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPastMeetingWithFutureMeetingId () {
        int id = cMgrHasContacts.addFutureMeeting(meetingContacts, futureDate);

        cMgrHasContacts.getPastMeeting(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPastMeetingWithFutureDate () {
        cMgrHasContacts.addNewPastMeeting(meetingContacts, futureDate, testMeetingNotes);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPastMeetingWithNullContacts () {
        cMgrHasContacts.addNewPastMeeting(null, pastDate, testMeetingNotes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPastMeetingWithEmptyContacts () {
        cMgrHasContacts.addNewPastMeeting(new HashSet<>(), pastDate, testMeetingNotes);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPastMeetingWithNullDate () {
        cMgrHasContacts.addNewPastMeeting(meetingContacts, null, testMeetingNotes);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPastMeetingWithNullNotes () {
        cMgrHasContacts.addNewPastMeeting(meetingContacts, pastDate, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPastMeetingWithEmptyNotes () {
        cMgrHasContacts.addNewPastMeeting(meetingContacts, pastDate, "");
    }

    /* =================== MEETING LISTS =================== */

    @Test
    public void testGetEmptyFutureMeetingListIsEmpty() {
        Set<Contact> contactSet = cMgrHasContacts.getContacts(1);
        Contact testContact = null;

        // seems odd that we're using a set - there's no "get()" method!!!
        // so we have to loop!
        for (Contact c: contactSet) {
            testContact = c;
        }

        List<Meeting> futureMeetingList = cMgrHasContacts.getFutureMeetingList(testContact);

        assertNotNull(futureMeetingList);
        assertEquals(futureMeetingList.size(), 0);
        assertTrue(futureMeetingList.isEmpty());
    }

    @Test
    public void testGetFutureMeetingListIsNotEmptyAndSorted() {
        Set<Contact> contactSet = cMgrHasContacts.getContacts(1);
        Set<Contact> meetingSet = cMgrHasContacts.getContacts(1,3,4,6,7,8);
        Contact testContact = null;

        for (Contact c: contactSet) {
            testContact = c;
        }

        // PAST
        Calendar date1 = new GregorianCalendar(2015, 11, 22);
        // FUTURE
        Calendar futureDate2 = new GregorianCalendar(2017, 4, 20);
        Calendar futureDate3 = new GregorianCalendar(2017, 3, 20);
        Calendar date4 = new GregorianCalendar(2014, 10, 30);

        cMgrHasContacts.addFutureMeeting(meetingSet, futureDate); // ID: 1
        cMgrHasContacts.addNewPastMeeting(meetingSet, date1, "Notes about meeting 1"); // ID: 2
        cMgrHasContacts.addFutureMeeting(meetingSet, futureDate2); // ID: 3
        cMgrHasContacts.addFutureMeeting(meetingSet, futureDate3); // ID: 4
        cMgrHasContacts.addNewPastMeeting(meetingSet, date4, "Notes about meeting 4"); // ID: 5
        List<Meeting> futureMeetingList = cMgrHasContacts.getFutureMeetingList(testContact);

        assertNotNull(futureMeetingList);
        assertEquals(futureMeetingList.size(), 3);
        assertFalse(futureMeetingList.isEmpty());

        Meeting mtg4 = futureMeetingList.get(0);
        Meeting mtg1 = futureMeetingList.get(1);
        Meeting mtg3 = futureMeetingList.get(2);

        assertEquals(mtg1.getId(), 1);
        assertEquals(mtg3.getId(), 3);
        assertEquals(mtg4.getId(), 4);

        assertEquals(mtg1.getContacts(), meetingSet);
        assertEquals(mtg3.getContacts(), meetingSet);
        assertEquals(mtg4.getContacts(), meetingSet);

        assertEquals(mtg1.getDate(), futureDate);
        assertEquals(mtg3.getDate(), futureDate2);
        assertEquals(mtg4.getDate(), futureDate3);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFutureMeetingListWithNullContactShouldThrow () {
        cMgrHasContacts.getFutureMeetingList(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFutureMeetingListWithInvalidContactShouldThrow () {
        cMgrHasContacts.getFutureMeetingList(new ContactImpl(99,"Jack Daniels"));
    }

    @Test
    public void testGetMeetingListOnIsEmpty() {
        List<Meeting> meetingList = cMgrHasContacts.getMeetingListOn(new GregorianCalendar(2018, 4, 30));

        assertNotNull(meetingList);
        assertEquals(meetingList.size(), 0);
        assertTrue(meetingList.isEmpty());
    }

    @Test
    public void testGetMeetingListOnFuture() {
        Set<Contact> meetingSet = cMgrHasContacts.getContacts(1,3,4,6,7,8);
        Calendar testDate = new GregorianCalendar(2016, 11, 10, 9, 30);
        Calendar testDate2 = new GregorianCalendar(2016, 11, 10, 14, 30);
        Calendar testDate3 = new GregorianCalendar(2016, 11, 10, 11, 0);

        cMgrHasContacts.addFutureMeeting(meetingSet, testDate); // ID: 1
        cMgrHasContacts.addNewPastMeeting(meetingSet, new GregorianCalendar(2012, 11, 18), "Notes"); // ID: 2
        cMgrHasContacts.addFutureMeeting(meetingSet, testDate2); // ID: 3
        cMgrHasContacts.addFutureMeeting(meetingSet, testDate3); // ID: 4
        cMgrHasContacts.addNewPastMeeting(meetingSet, new GregorianCalendar(2012, 11, 16), "Notes"); // ID: 5

        List<Meeting> meetingList = cMgrHasContacts.getMeetingListOn(testDate);

        assertNotNull(meetingList);
        assertEquals(meetingList.size(), 3);
        assertFalse(meetingList.isEmpty());

        Meeting mtg1 = meetingList.get(0);
        Meeting mtg2 = meetingList.get(1);
        Meeting mtg3 = meetingList.get(2);

        assertEquals(mtg1.getId(), 1);
        assertEquals(mtg2.getId(), 4);
        assertEquals(mtg3.getId(), 3);

        assertEquals(mtg1.getContacts(), meetingSet);
        assertEquals(mtg2.getContacts(), meetingSet);
        assertEquals(mtg3.getContacts(), meetingSet);

        assertEquals(mtg1.getDate(), testDate);
        assertEquals(mtg2.getDate(), testDate3);
        assertEquals(mtg3.getDate(), testDate2);
    }

    @Test
    public void testGetMeetingListOnPast() {
        Set<Contact> meetingSet = cMgrHasContacts.getContacts(1,3,2);
        Calendar testDate = new GregorianCalendar(1979, 7, 10, 9, 30);
        Calendar testDate2 = new GregorianCalendar(1979, 7, 10, 14, 30);
        Calendar testDate3 = new GregorianCalendar(1979, 7, 10, 11, 0);

        cMgrHasContacts.addFutureMeeting(meetingSet, new GregorianCalendar(2016, 11, 20)); // ID: 1
        cMgrHasContacts.addNewPastMeeting(meetingSet, testDate, "Notes"); // ID: 2
        cMgrHasContacts.addFutureMeeting(meetingSet, new GregorianCalendar(2016, 11, 20)); // ID: 3
        cMgrHasContacts.addFutureMeeting(meetingSet, new GregorianCalendar(2016, 11, 20)); // ID: 4
        cMgrHasContacts.addNewPastMeeting(meetingSet, testDate2, "Notes"); // ID: 5
        cMgrHasContacts.addNewPastMeeting(meetingSet, testDate3, "Notes"); // ID: 6

        List<Meeting> meetingList = cMgrHasContacts.getMeetingListOn(new GregorianCalendar(1979, 7, 10));

        assertNotNull(meetingList);
        assertEquals(meetingList.size(), 3);
        assertFalse(meetingList.isEmpty());

        Meeting mtg1 = meetingList.get(0);
        Meeting mtg2 = meetingList.get(1);
        Meeting mtg3 = meetingList.get(2);

        assertEquals(mtg1.getId(), 2);
        assertEquals(mtg2.getId(), 6);
        assertEquals(mtg3.getId(), 5);

        assertEquals(mtg1.getContacts(), meetingSet);
        assertEquals(mtg2.getContacts(), meetingSet);
        assertEquals(mtg3.getContacts(), meetingSet);

        assertEquals(mtg1.getDate(), testDate);
        assertEquals(mtg2.getDate(), testDate3);
        assertEquals(mtg3.getDate(), testDate2);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMeetingListOnShouldThrowForNullDate() {
        cMgrHasContacts.getMeetingListOn(null);
    }

    @Test
    public void testGetEmptyPastMeetingListForIsEmpty() {
        Set<Contact> contactSet = cMgrHasContacts.getContacts(1);
        Contact testContact = null;

        for (Contact c: contactSet) {
            testContact = c;
        }

        List<PastMeeting> pastMeetingList = cMgrHasContacts.getPastMeetingListFor(testContact);

        assertNotNull(pastMeetingList);
        assertEquals(pastMeetingList.size(), 0);
        assertTrue(pastMeetingList.isEmpty());
    }

    @Test
    public void testGetPastMeetingListForIsNotEmptyAndSorted() {
        Set<Contact> contactSet = cMgrHasContacts.getContacts(1);
        Set<Contact> meetingSet = cMgrHasContacts.getContacts(1,2,3);
        Contact testContact = null;

        for (Contact c: contactSet) {
            testContact = c;
        }

        // PAST expected output order: 2, 3, 4, 1
        Calendar date1 = new GregorianCalendar(2015, 11, 22);
        Calendar date2 = new GregorianCalendar(2013, 9, 2);
        Calendar date3 = new GregorianCalendar(2014, 4, 14);
        Calendar date4 = new GregorianCalendar(2014, 10, 30);
        // FUTURE
        Calendar futureDate2 = new GregorianCalendar(2017, 4, 20);
        Calendar futureDate3 = new GregorianCalendar(2017, 3, 20);

        cMgrHasContacts.addFutureMeeting(meetingSet, futureDate); // ID: 1
        cMgrHasContacts.addNewPastMeeting(meetingSet, date1, "Notes"); // ID: 2
        cMgrHasContacts.addFutureMeeting(meetingSet, futureDate2); // ID: 3
        cMgrHasContacts.addNewPastMeeting(meetingSet, date2, "Notes"); // ID: 4
        cMgrHasContacts.addFutureMeeting(meetingSet, futureDate3); // ID: 5
        cMgrHasContacts.addNewPastMeeting(meetingSet, date3, "Notes"); // ID: 6
        cMgrHasContacts.addNewPastMeeting(meetingSet, date4, "Notes"); // ID: 7
        List<PastMeeting> pastMeetings = cMgrHasContacts.getPastMeetingListFor(testContact);

        assertNotNull(pastMeetings);
        assertEquals(pastMeetings.size(), 4);
        assertFalse(pastMeetings.isEmpty());

        // expected meeting ID order 4, 6, 7, 2
        Meeting mtg4 = pastMeetings.get(0);
        Meeting mtg6 = pastMeetings.get(1);
        Meeting mtg7 = pastMeetings.get(2);
        Meeting mtg2 = pastMeetings.get(3);

        assertEquals(mtg4.getId(), 4);
        assertEquals(mtg6.getId(), 6);
        assertEquals(mtg7.getId(), 7);
        assertEquals(mtg2.getId(), 2);

        assertEquals(mtg4.getContacts(), meetingSet);
        assertEquals(mtg6.getContacts(), meetingSet);
        assertEquals(mtg7.getContacts(), meetingSet);
        assertEquals(mtg2.getContacts(), meetingSet);

        assertEquals(mtg4.getDate(), date2);
        assertEquals(mtg6.getDate(), date3);
        assertEquals(mtg7.getDate(), date4);
        assertEquals(mtg2.getDate(), date1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPastMeetingListForWithNullContactShouldThrow () {
        cMgrHasContacts.getPastMeetingListFor(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPastMeetingListForWithInvalidContactShouldThrow () {
        cMgrHasContacts.getPastMeetingListFor(new ContactImpl(99,"Jack Daniels"));
    }

    @Test
    public void testAddMeetingNotes () {
        Set<Contact> meetingSet = cMgrHasContacts.getContacts(1,2,3);
        Calendar futureTime = new GregorianCalendar();
        futureTime.add(Calendar.MILLISECOND, 1);
        cMgrHasContacts.addFutureMeeting(meetingSet, futureTime);

        try {
            Thread.sleep(5);
        } catch (InterruptedException iEx) {
            iEx.printStackTrace();
        }

        cMgrHasContacts.addMeetingNotes(1, "Notes");

        assertEquals(cMgrHasContacts.getPastMeeting(1).getNotes(), "Notes");
    }

    @Test
    public void testAppendMeetingNotes () {
        Set<Contact> meetingSet = cMgrHasContacts.getContacts(1,2,3);
        cMgrHasContacts.addNewPastMeeting(meetingSet, pastDate, "Notes");
        cMgrHasContacts.addMeetingNotes(1, "Some more notes");
        cMgrHasContacts.addMeetingNotes(1, "Even more notes");

        assertEquals(cMgrHasContacts.getPastMeeting(1).getNotes(), "Notes\nSome more notes\nEven more notes");
    }

    @Test(expected = NullPointerException.class)
    public void testAddMeetingNotesWithNullNotes () {
        cMgrHasContacts.addMeetingNotes(1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMeetingNotesWithInvalidId () {
        cMgrHasContacts.addMeetingNotes(99, "Notes");
    }

    @Test(expected = IllegalStateException.class)
    public void testAddMeetingNotesToFutureMeeting () {
        Set<Contact> meetingSet = cMgrHasContacts.getContacts(1,2,3);
        cMgrHasContacts.addFutureMeeting(meetingSet, futureDate);
        cMgrHasContacts.addMeetingNotes(1, "Notes");
    }
}
