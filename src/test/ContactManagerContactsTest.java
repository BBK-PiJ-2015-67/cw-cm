package test;

import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * ContactManager tests
 * Split up the tests as the main test class was getting quite large
 *
 * This class tests the Contact methods in ContactManager
 * @author lmignot
 */
public class ContactManagerContactsTest {

    private ContactManager cMgr;
    private ContactManager cMgrHasContacts;

    @Before
    public void setUp() {
        // delete any data file if present before running tests
        TestUtils.deleteDataFile();

        cMgr = new ContactManagerImpl();
        cMgrHasContacts = new ContactManagerImpl();

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
    }

    @After
    public void tearDown() {
        // delete any data file if present after running tests
        TestUtils.deleteDataFile();

        cMgr = null;
        cMgrHasContacts = null;
    }

    /* =================== CONTACTS =================== */

    @Test
    public void testAddContactsToCM () {
        cMgr.addNewContact("Xenia Garand", "This one's xenophobic");
        cMgr.addNewContact("Sherlene Westrich", "From the west");
        cMgr.addNewContact("Emmaline Cupit", "Cupid's daughter");
        cMgr.addNewContact("Kendra Kinghorn", "Said to hold the secret to the legendary horn");
        cMgr.addNewContact("Aaron Kamen", "Come in... get it?");
        cMgr.addNewContact("Ellis Pollak", "Probably a genius come to think of it");
        cMgr.addNewContact("Carrol Sin", "Christmas is his favourite time");
        cMgr.addNewContact("Norman Wiedemann", "AKA the weed slayer");
        cMgr.addNewContact("Efren Apodaca", "There's a pharmacist in his future...");
        cMgr.addNewContact("Errol Flynn", "The ultimate swashbuckler");

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
        Set<Contact> testContacts = cMgrHasContacts.getContacts(1, 2, 3);

        assertNotNull(testContacts);
        assertEquals(testContacts.size(),3);

        boolean testPassed = false;
        for(Contact c : testContacts) {
            if (c.getName().equals("Aaron Kamen") ||
                    c.getName().equals("Xenia Garand") ||
                    c.getName().equals("Sherlene Westrich")) {
                testPassed = true;
            }
        }
        assertTrue(testPassed);
    }

    @Test
    public void testGetContactsByIdsWithDuplicates () {
        Set<Contact> testContacts = cMgrHasContacts.getContacts(1, 2, 3, 1);

        assertNotNull(testContacts);
        assertEquals(testContacts.size(),3);

        boolean testPassed = false;
        for(Contact c : testContacts) {
            if (c.getName().equals("Aaron Kamen") ||
                    c.getName().equals("Xenia Garand") ||
                    c.getName().equals("Sherlene Westrich")) {
                testPassed = true;
            }
        }
        assertTrue(testPassed);
    }

    @Test(expected = IllegalArgumentException.class)
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

    @Test(expected = IllegalArgumentException.class)
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
