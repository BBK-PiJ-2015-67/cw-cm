package test;

import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

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

        cMgrHasContacts.addNewContact("Aaron Kamen", "Camen get it!");
        cMgrHasContacts.addNewContact("Xenia Garand", "This one's xenophobic");
        cMgrHasContacts.addNewContact("Sherlene Westrich", "From the west");
        cMgrHasContacts.addNewContact("Emmaline Cupit", "Cupid's daughter");
        cMgrHasContacts.addNewContact("Kendra Kinghorn", "Said to hold the secret to the legendary horn");
        cMgrHasContacts.addNewContact("Ellis Pollak", "Unfortunate naming...");
        cMgrHasContacts.addNewContact("Carrol Sin", "Christmas is his favourite time");
        cMgrHasContacts.addNewContact("Norman Wiedemann", "XXXXL");
        cMgrHasContacts.addNewContact("Efren Apodaca", "There's a pharmacist in his future...");
        cMgrHasContacts.addNewContact("Floyd Drager", "In France we say this man is popular with..");

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

    @Test
    public void testAddFutureMeeting () {
        int futureMeetingId = cMgrHasContacts.addFutureMeeting(meetingContacts, futureDate);

        assertEquals(futureMeetingId,1);
    }

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

    @Test
    public void testAddContactsToCM () {
        int contact1 = cMgr.addNewContact("Xenia Garand", "This one's xenophobic");
        int contact2 = cMgr.addNewContact("Sherlene Westrich", "From the west");
        int contact3 = cMgr.addNewContact("Emmaline Cupit", "Cupid's daughter");
        int contact4 = cMgr.addNewContact("Kendra Kinghorn", "Said to hold the secret to the legendary horn");
        int contact5 = cMgr.addNewContact("Aaron Kamen", "Camen get it!");
        int contact6 = cMgr.addNewContact("Ellis Pollak", "Unfortunate naming...");
        int contact7 = cMgr.addNewContact("Carrol Sin", "Christmas is his favourite time");
        int contact8 = cMgr.addNewContact("Norman Wiedemann", "XXXXL");
        int contact9 = cMgr.addNewContact("Efren Apodaca", "There's a pharmacist in his future...");
        int contact10 = cMgr.addNewContact("Floyd Drager", "In France we say this man is popular with..");

        assertNotEquals(cMgr.getContacts(""), new HashSet<Contact>());
        assertNotNull(cMgr.getContacts(""));
        assertEquals(cMgr.getContacts("").size(),10);
    }

    @Test
    public void testThatNewCMHasEmptyContacts () {
        assertEquals(cMgr.getContacts(""),new HashSet<Contact>());
        assertNotNull(cMgr.getContacts(""));
        assertEquals(cMgr.getContacts("").size(),0);
    }

    @Test
    public void testGetSpecificContactsFromCM () {
        int a = cMgr.addNewContact("Aaron Kamen", "Camen get it!");
        int b = cMgr.addNewContact("Aaron Kamen", "Camen get it!");
        int c = cMgr.addNewContact("Xenia Garand", "This one's xenophobic");
        int d = cMgr.addNewContact("Sherlene Westrich", "From the west");
        int e = cMgr.addNewContact("Emmaline Cupit", "Cupid's daughter");
        int f = cMgr.addNewContact("Kendra Kinghorn", "Said to hold the secret to the legendary horn");
        int g = cMgr.addNewContact("Aaron Kamen", "Camen get it!");
        int h = cMgr.addNewContact("Ellis Pollak", "Unfortunate naming...");
        int i = cMgr.addNewContact("Carrol Sin", "Christmas is his favourite time");
        int j = cMgr.addNewContact("Norman Wiedemann", "XXXXL");
        int k = cMgr.addNewContact("Efren Apodaca", "There's a pharmacist in his future...");
        int l = cMgr.addNewContact("Floyd Drager", "In France we say this man is popular with..");

        Set<Contact> testContacts = cMgr.getContacts("Aaron Kamen");

        assertNotEquals(testContacts, new HashSet<Contact>());
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

        assertNotEquals(testContacts, new HashSet<Contact>());
        assertNotNull(testContacts);
        assertEquals(testContacts.size(),2);
    }

    @Test
    public void testGetContactsStringWhenCMHasNoContacts () {
        Set<Contact> testContacts = cMgr.getContacts("Floyd Drager");

        assertEquals(testContacts, new HashSet<Contact>());
    }

    @Test
    public void testGetContactsByIds () {
        cMgr.addNewContact("Aaron Kamen", "Camen get it!");
        cMgr.addNewContact("Xenia Garand", "This one's xenophobic");
        cMgr.addNewContact("Sherlene Westrich", "From the west");
        cMgr.addNewContact("Emmaline Cupit", "Cupid's daughter");
        cMgr.addNewContact("Kendra Kinghorn", "Said to hold the secret to the legendary horn");
        cMgr.addNewContact("Ellis Pollak", "Unfortunate naming...");
        cMgr.addNewContact("Carrol Sin", "Christmas is his favourite time");
        cMgr.addNewContact("Norman Wiedemann", "XXXXL");
        cMgr.addNewContact("Efren Apodaca", "There's a pharmacist in his future...");
        cMgr.addNewContact("Floyd Drager", "In France we say this man is popular with..");

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
        cMgr.addNewContact("Aaron Kamen", "Camen get it!");
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
