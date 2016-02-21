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
 *
 * @author lmignot
 */
public class ContactManagerTest {

    private ContactManager cMgr;
    private ContactManager cMgrHasContacts;
    private Set<Contact> meetingContacts;

    private Calendar futureDate;
    private Calendar pastDate;
    private Calendar now;

    private String testMeetingNotes;

    @Before
    public void setUp() {
        cMgr = new ContactManagerImpl();
        cMgrHasContacts = new ContactManagerImpl();

        now = new GregorianCalendar();
        futureDate = new GregorianCalendar();
        pastDate = new GregorianCalendar();

        futureDate.set(Calendar.DATE, now.get(Calendar.DATE) + 2);
        pastDate.set(Calendar.DATE, now.get(Calendar.DATE) - 2);

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
        cMgr = null;
        cMgrHasContacts = null;
        meetingContacts = null;
        now = null;
        futureDate = null;
        pastDate = null;
    }

    /* =================== CONSTRUCTOR, FLUSH, ETC... =================== */

    @Test
    public void testConstructAContactManager () {
        cMgr = new ContactManagerImpl();
        assertNotNull(cMgr);
    }

    @Test
    public void testConstructAUniqueContactManager () {
        ContactManager cm = new ContactManagerImpl();

        assertNotNull(cm);

        assertNotNull(cMgr);
        assertNotEquals(cMgr,cm);
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

        assertEquals(futureMeetingId,1);
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

    @Test(expected = IllegalStateException.class)
    public void testGetPastMeetingWithFutureMeetingId () {
        int id = cMgrHasContacts.addFutureMeeting(meetingContacts, futureDate);

        cMgrHasContacts.getPastMeeting(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPastMeetingWithCurrentDate () {
        cMgrHasContacts.addNewPastMeeting(meetingContacts, now, testMeetingNotes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPastMeetingWithFutureDate () {
        cMgrHasContacts.addNewPastMeeting(meetingContacts, futureDate, testMeetingNotes);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPastMeetingWithNullContacts () {
        cMgrHasContacts.addNewPastMeeting(null, pastDate, testMeetingNotes);
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
            testContact = new ContactImpl(c.getId(), c.getName(), c.getNotes());
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

        // seems odd that we're using a set - there's no "get()" method!!!
        // so we have to loop... to get ONE contact!
        for (Contact c: contactSet) {
            testContact = new ContactImpl(c.getId(), c.getName(), c.getNotes());
        }

        // PAST
        Calendar date1 = new GregorianCalendar();
        date1.set(Calendar.DATE, pastDate.get(Calendar.DATE));
        // FUTURE
        Calendar futureDate2 = new GregorianCalendar();
        futureDate2.set(Calendar.DATE, futureDate.get(Calendar.DATE) + 5);
        Calendar futureDate3 = new GregorianCalendar();
        futureDate3.set(Calendar.DATE, futureDate2.get(Calendar.DATE) + 2);
        // PAST
        Calendar date4 = new GregorianCalendar();
        date4.set(Calendar.DATE, futureDate3.get(Calendar.DATE) - 21);

        cMgrHasContacts.addFutureMeeting(meetingSet, futureDate); // ID: 1
        cMgrHasContacts.addNewPastMeeting(meetingSet, date1, "Notes about meeting 1"); // ID: 2
        cMgrHasContacts.addFutureMeeting(meetingSet, futureDate2); // ID: 3
        cMgrHasContacts.addFutureMeeting(meetingSet, futureDate3); // ID: 4
        cMgrHasContacts.addNewPastMeeting(meetingSet, date4, "Notes about meeting 4"); // ID: 5
        List<Meeting> futureMeetingList = cMgrHasContacts.getFutureMeetingList(testContact);

        assertNotNull(futureMeetingList);
        assertEquals(futureMeetingList.size(), 3);
        assertFalse(futureMeetingList.isEmpty());

        Meeting mtg1 = futureMeetingList.get(0);
        Meeting mtg4 = futureMeetingList.get(1);
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

    /* =================== CONTACTS =================== */

    @Test
    public void testAddContactsToCM () {
        int contact1 = cMgr.addNewContact("Xenia Garand", "This one's xenophobic");
        int contact2 = cMgr.addNewContact("Sherlene Westrich", "From the west");
        int contact3 = cMgr.addNewContact("Emmaline Cupit", "Cupid's daughter");
        int contact4 = cMgr.addNewContact("Kendra Kinghorn", "Said to hold the secret to the legendary horn");
        int contact5 = cMgr.addNewContact("Aaron Kamen", "Come in... get it?");
        int contact6 = cMgr.addNewContact("Ellis Pollak", "Probably a genius come to think of it");
        int contact7 = cMgr.addNewContact("Carrol Sin", "Christmas is his favourite time");
        int contact8 = cMgr.addNewContact("Norman Wiedemann", "AKA the weed slayer");
        int contact9 = cMgr.addNewContact("Efren Apodaca", "There's a pharmacist in his future...");
        int contact10 = cMgr.addNewContact("Errol Flynn", "The ultimate swashbuckler");

        assertFalse(cMgr.getContacts("").isEmpty());
        assertNotNull(cMgr.getContacts(""));
        assertEquals(cMgr.getContacts("").size(),10);
    }

    @Test
    public void testThatNewCMHasEmptyContacts () {
        assertTrue(cMgr.getContacts("").isEmpty());
        assertNotNull(cMgr.getContacts(""));
        assertEquals(cMgr.getContacts("").size(),0);
    }

    @Test
    public void testGetSpecificContactsFromCM () {
        int a = cMgr.addNewContact("Aaron Kamen", "Come in... get it?");
        int b = cMgr.addNewContact("Aaron Kamen", "Come in... get it?");
        int c = cMgr.addNewContact("Xenia Garand", "This one's xenophobic");
        int d = cMgr.addNewContact("Sherlene Westrich", "From the west");
        int e = cMgr.addNewContact("Emmaline Cupit", "Cupid's daughter");
        int f = cMgr.addNewContact("Kendra Kinghorn", "Said to hold the secret to the legendary horn");
        int g = cMgr.addNewContact("Aaron Kamen", "Come in... get it?");
        int h = cMgr.addNewContact("Ellis Pollak", "Probably a genius come to think of it");
        int i = cMgr.addNewContact("Carrol Sin", "Christmas is his favourite time");
        int j = cMgr.addNewContact("Norman Wiedemann", "AKA the weed slayer");
        int k = cMgr.addNewContact("Efren Apodaca", "There's a pharmacist in his future...");
        int l = cMgr.addNewContact("Errol Flynn", "The ultimate swashbuckler");

        Set<Contact> testContacts = cMgr.getContacts("Aaron Kamen");

        assertFalse(testContacts.isEmpty());
        assertNotNull(testContacts);
        assertEquals(testContacts.size(),3);

        assertNotEquals(a, b);
        assertNotEquals(a, g);
        assertNotEquals(b, g);
    }

    @Test
    public void testGetContactsByNameDoesNotIgnoreWhitespace () {
        cMgr.addNewContact("Sherlene Westrich", "From the west");
        // This one below has an extra space at the end
        cMgr.addNewContact("Sherlene Westrich ", "From the west");
        cMgr.addNewContact("Sherlene Westrich", "From the west");

        Set<Contact> testContacts = cMgr.getContacts("Sherlene Westrich");

        assertFalse(testContacts.isEmpty());
        assertNotNull(testContacts);
        assertEquals(testContacts.size(),2);
    }

    @Test
    public void testGetContactsStringWhenCMHasNoContacts () {
        Set<Contact> testContacts = cMgr.getContacts("Floyd Drager");

        assertTrue(testContacts.isEmpty());
        assertEquals(testContacts.size(),0);
    }

    @Test
    public void testGetContactsStringWithNonExistentContact () {
        Set<Contact> testContacts = cMgrHasContacts.getContacts("John Doe");

        assertTrue(testContacts.isEmpty());
        assertEquals(testContacts.size(),0);
    }

    @Test
    public void testGetContactsByIds () {
        cMgr.addNewContact("Aaron Kamen", "Come in... get it?");
        cMgr.addNewContact("Xenia Garand", "This one's xenophobic");
        cMgr.addNewContact("Sherlene Westrich", "From the west");
        cMgr.addNewContact("Emmaline Cupit", "Cupid's daughter");
        cMgr.addNewContact("Kendra Kinghorn", "Said to hold the secret to the legendary horn");
        cMgr.addNewContact("Ellis Pollak", "Probably a genius come to think of it");
        cMgr.addNewContact("Carrol Sin", "Christmas is his favourite time");
        cMgr.addNewContact("Norman Wiedemann", "AKA the weed slayer");
        cMgr.addNewContact("Efren Apodaca", "There's a pharmacist in his future...");
        cMgr.addNewContact("Errol Flynn", "The ultimate swashbuckler");

        Set<Contact> testContacts = cMgr.getContacts(1, 2, 3);

        assertNotNull(testContacts);
        assertEquals(testContacts.size(),3);

        boolean testPassed = false;
        for(Contact c : testContacts) {
            System.out.println(c.getId() + ": " + c.getName());
            if (c.getName().equals("Aaron Kamen") ||
                    c.getName().equals("Xenia Garand") ||
                    c.getName().equals("Sherlene Westrich")) {
                testPassed = true;
            }
        }
        assertTrue(testPassed);
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsIdsWithInvalidIds () {
        cMgr.addNewContact("Aaron Kamen", "Come in... get it?");
        cMgr.addNewContact("Xenia Garand", "This one's xenophobic");
        cMgr.addNewContact("Sherlene Westrich", "From the west");
        cMgr.addNewContact("Emmaline Cupit", "Cupid's daughter");

        cMgr.getContacts(2, 9, 23, 1, 3);
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsIdsWithNull () {
        int[] l = null;
        cMgr.getContacts(l);
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsIdsWhenCMHasNoContacts () {
        cMgr.getContacts(2, 9, 23, 1, 3);
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsStringWithNull () {
        String s = null;
        cMgr.getContacts(s);
    }

    @Test(expected = NullPointerException.class)
    public void testAddContactsWithNullName () {
        cMgr.addNewContact(null, "This one's xenophobic");
    }

    @Test(expected = NullPointerException.class)
    public void testAddContactsWithNullNotes () {
        cMgr.addNewContact("Floyd Drager", null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddContactsWithNullNameAndNotes () {
        cMgr.addNewContact(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContactsWithEmptyName () {
        cMgr.addNewContact("", "This one's xenophobic");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContactsWithEmptyNotes () {
        cMgr.addNewContact("Floyd Drager", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContactsWithEmptyNameAndNotes () {
        cMgr.addNewContact("", "");
    }
}
