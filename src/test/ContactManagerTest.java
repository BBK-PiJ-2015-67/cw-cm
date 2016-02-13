package test;

import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;
import spec.Meeting;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ContactManager tests
 *
 * @author lmignot
 */
public class ContactManagerTest {

    private ContactManager cMgr;
    private int pastMeetingId = 1;
    private int futureMeetingId = 0;

    private Calendar futureDate;
    private Calendar pastDate;
    private Calendar now;

    @Before
    public void setUp() {
        cMgr = new ContactManagerImpl();
        now = new GregorianCalendar();
        futureDate = new GregorianCalendar();
        pastDate = new GregorianCalendar();

        futureDate.set(Calendar.DATE, now.get(Calendar.DATE) + 2);
        pastDate.set(Calendar.DATE, now.get(Calendar.DATE) - 2);
    }

    @After
    public void tearDown() {
        cMgr = null;
    }

    @Test
    public void testConstructAContactManager () {
        cMgr = new ContactManagerImpl();
        assertThat(cMgr).isNotNull();
    }

    @Test
    public void testConstructAUniqueContactManager () {
        ContactManager cm = new ContactManagerImpl();

        assertThat(cm).isNotNull();

        assertThat(cMgr).isNotNull();
        assertThat(cMgr).isNotEqualTo(cm);
    }

    @Test
    public void testGetMeeting () {
        Meeting mtg = cMgr.getMeeting(0);
        assertThat(mtg).isNotNull();
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

        assertThat(cMgr.getContacts("")).isNotEmpty();
        assertThat(cMgr.getContacts("")).isNotNull();
        assertThat(cMgr.getContacts("").size()).isEqualTo(10);
    }

    @Test
    public void testThatNewCMHasEmptyContacts () {
        assertThat(cMgr.getContacts("")).isEmpty();
        assertThat(cMgr.getContacts("")).isNotNull();
        assertThat(cMgr.getContacts("").size()).isEqualTo(0);
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

        assertThat(testContacts).isNotEmpty();
        assertThat(testContacts).isNotNull();
        assertThat(testContacts.size()).isEqualTo(3);

        assertThat(a).isNotEqualTo(b).isNotEqualTo(g);
        assertThat(b).isNotEqualTo(g);
    }

    @Test
    public void testGetContactsByNameDoesNotIgnoreWhitespace () {
        int a = cMgr.addNewContact("Sherlene Westrich", "From the west");
        // This one below has an extra space at the end
        int b = cMgr.addNewContact("Sherlene Westrich ", "From the west");
        int c = cMgr.addNewContact("Sherlene Westrich", "From the west");

        Set<Contact> testContacts = cMgr.getContacts("Sherlene Westrich");

        assertThat(testContacts).isNotEmpty();
        assertThat(testContacts).isNotNull();
        assertThat(testContacts.size()).isEqualTo(2);
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

        assertThat(testContacts).isNotEmpty();
        assertThat(testContacts).isNotNull();
        assertThat(testContacts.size()).isEqualTo(3);

        boolean testPassed = false;
        for(Contact c : testContacts) {
            System.out.println(c.getId() + ": " + c.getName());
            if (c.getName().equals("Aaron Kamen") ||
                    c.getName().equals("Xenia Garand") ||
                    c.getName().equals("Sherlene Westrich")) {
                testPassed = true;
            }
        }
        assertThat(testPassed).isTrue();
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
