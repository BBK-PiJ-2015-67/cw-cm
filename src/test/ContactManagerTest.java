package test;

import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ContactManager tests
 *
 * @author lmignot
 */
public class ContactManagerTest {

    private ContactManager cMgr;

    @Before
    public void setUp() {
        cMgr = new ContactManagerImpl();
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
