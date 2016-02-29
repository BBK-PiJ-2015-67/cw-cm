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

    // some data to work with
    private static final String CONTACT_1_NAME = "Han Solo";
    private static final String CONTACT_1_NOTES = "Shoots first.";
    private static final int CONTACT_1_ID = 1;

    private static final String CONTACT_2_NAME = "Luke Skywalker";
    private static final String CONTACT_2_NOTES = "Daddy Issues. Short-handed.";
    private static final int CONTACT_2_ID = 2;

    private static final String CONTACT_3_NAME = "Princess Leia";
    private static final String CONTACT_3_NAME_WHITESPACE = "Princess  Leia";
    private static final String CONTACT_3_NAME_PREFIX = " Princess Leia";
    private static final String CONTACT_3_NAME_SUFFIX = "Princess Leia ";
    private static final String CONTACT_3_NOTES = "Hair needs help.";
    private static final int CONTACT_3_ID = 3;

    private static final String CONTACT_4_NAME = "Darth Vader";
    private static final String CONTACT_4_NOTES = "Short-handed redux.";
    private static final int CONTACT_4_ID = 4;

    private static final int EXPECTED_CONTACT_SET_SIZE = 4;

    // for error checking
    private static final String NON_EXISTENT_CONTACT_NAME = "Obi Wan Kenobi";
    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = null;
    private static final int[] NULL_ARRAY = null;
    private static final int EMPTY_SIZE = 0;
    private static final int ILLEGAL_ID_1 = 6;
    private static final int ILLEGAL_ID_2 = 90;
    private static final int ILLEGAL_ID_3 = 43;
    private static final int ILLEGAL_ID_4 = 987;

    private ContactManager emptyContactManager;
    private ContactManager contactManagerWithContacts;

    @Before
    public void setUp() {
        // delete any data file if present before running tests
        TestUtils.deleteDataFile();

        emptyContactManager = new ContactManagerImpl();
        contactManagerWithContacts = new ContactManagerImpl();

        contactManagerWithContacts.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);
        contactManagerWithContacts.addNewContact(CONTACT_2_NAME, CONTACT_2_NOTES);
        contactManagerWithContacts.addNewContact(CONTACT_3_NAME, CONTACT_3_NOTES);
        contactManagerWithContacts.addNewContact(CONTACT_4_NAME, CONTACT_4_NOTES);
    }

    @After
    public void tearDown() {
        // delete any data file if present after running tests
        TestUtils.deleteDataFile();

        emptyContactManager = null;
        contactManagerWithContacts = null;
    }

    /* =================== CONTACTS =================== */

    @Test
    public void testAddContactsToEmptyCM () {
        emptyContactManager.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);
        emptyContactManager.addNewContact(CONTACT_2_NAME, CONTACT_2_NOTES);
        emptyContactManager.addNewContact(CONTACT_3_NAME, CONTACT_3_NOTES);
        emptyContactManager.addNewContact(CONTACT_4_NAME, CONTACT_4_NOTES);

        assertNotNull(emptyContactManager.getContacts(EMPTY_STRING));
        assertFalse(emptyContactManager.getContacts(EMPTY_STRING).isEmpty());
        assertEquals(emptyContactManager.getContacts(EMPTY_STRING).size(), EXPECTED_CONTACT_SET_SIZE);
    }

    @Test
    public void testThatNewCMHasEmptyContacts () {
        assertNotNull(emptyContactManager.getContacts(EMPTY_STRING));
        assertTrue(emptyContactManager.getContacts(EMPTY_STRING).isEmpty());
        assertEquals(emptyContactManager.getContacts(EMPTY_STRING).size(), EMPTY_SIZE);
    }

    @Test
    public void testGetSpecificContactsByNameFromCM () {
        int dupContact1Id = emptyContactManager.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);
        int dupContact2Id = emptyContactManager.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);
        int dupContact3Id = emptyContactManager.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);

        Set<Contact> testContacts = emptyContactManager.getContacts(CONTACT_1_NAME);

        assertNotNull(testContacts);
        assertFalse(testContacts.isEmpty());
        assertEquals(testContacts.size(), 3);

        assertNotEquals(dupContact1Id, dupContact2Id);
        assertNotEquals(dupContact1Id, dupContact3Id);
        assertNotEquals(dupContact2Id, dupContact3Id);
    }

    @Test
    public void testGetContactsByNameDoesIgnoreWhitespace () {
        emptyContactManager.addNewContact(CONTACT_3_NAME, CONTACT_3_NOTES);
        emptyContactManager.addNewContact(CONTACT_3_NAME, CONTACT_3_NOTES);
        emptyContactManager.addNewContact(CONTACT_3_NAME_PREFIX, CONTACT_3_NOTES);
        emptyContactManager.addNewContact(CONTACT_3_NAME_SUFFIX, CONTACT_3_NOTES);
        emptyContactManager.addNewContact(CONTACT_3_NAME_WHITESPACE, CONTACT_3_NOTES);

        Set<Contact> testContacts = emptyContactManager.getContacts(CONTACT_3_NAME);

        assertNotNull(testContacts);
        assertFalse(testContacts.isEmpty());
        assertEquals(testContacts.size(), 4);
    }

    @Test
    public void testGetContactsStringWhenCMHasNoContacts () {
        Set<Contact> testContacts = emptyContactManager.getContacts(CONTACT_1_NAME);

        assertTrue(testContacts.isEmpty());
        assertEquals(testContacts.size(),0);
    }

    @Test
    public void testGetContactsStringWithNonExistentContact () {
        Set<Contact> testContacts = contactManagerWithContacts.getContacts(NON_EXISTENT_CONTACT_NAME);

        assertTrue(testContacts.isEmpty());
        assertEquals(testContacts.size(), EMPTY_SIZE);
    }

    @Test
    public void testGetContactsByIds () {
        Set<Contact> testContacts = contactManagerWithContacts.getContacts(
                CONTACT_1_ID,
                CONTACT_2_ID,
                CONTACT_3_ID,
                CONTACT_4_ID
        );

        assertNotNull(testContacts);
        assertEquals(testContacts.size(), EXPECTED_CONTACT_SET_SIZE);

        boolean testPassed = false;
        for(Contact c : testContacts) {
            testPassed = (c.getName().equals(CONTACT_1_NAME) ||
                          c.getName().equals(CONTACT_2_NAME) ||
                          c.getName().equals(CONTACT_3_NAME) ||
                          c.getName().equals(CONTACT_4_NAME));
        }
        assertTrue(testPassed);
    }

    @Test
    public void testGetContactsByIdsWithDuplicates () {
        Set<Contact> testContacts = contactManagerWithContacts.getContacts(
            CONTACT_1_ID,
            CONTACT_2_ID,
            CONTACT_3_ID,
            CONTACT_4_ID,
            CONTACT_3_ID,
            CONTACT_1_ID
        );

        assertNotNull(testContacts);
        assertEquals(testContacts.size(), EXPECTED_CONTACT_SET_SIZE);

        boolean testPassed = false;
        for(Contact c : testContacts) {
            testPassed = (c.getName().equals(CONTACT_1_NAME) ||
                          c.getName().equals(CONTACT_2_NAME) ||
                          c.getName().equals(CONTACT_3_NAME) ||
                          c.getName().equals(CONTACT_4_NAME));
        }
        assertTrue(testPassed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContactsIdsWithInvalidIds () {
        contactManagerWithContacts.getContacts(
            ILLEGAL_ID_1,
            ILLEGAL_ID_2,
            ILLEGAL_ID_3,
            ILLEGAL_ID_4
        );
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsIdsWithNull () {
        emptyContactManager.getContacts(NULL_ARRAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContactsIdsWhenCMHasNoContacts () {
        emptyContactManager.getContacts(
            CONTACT_1_ID,
            CONTACT_2_ID,
            CONTACT_3_ID,
            CONTACT_4_ID
        );
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsByNameWithNull () {
        emptyContactManager.getContacts(NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testAddContactsWithNullName () {
        emptyContactManager.addNewContact(NULL_STRING, CONTACT_1_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void testAddContactsWithNullNotes () {
        emptyContactManager.addNewContact(CONTACT_1_NAME, NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testAddContactsWithNullNameAndNotes () {
        emptyContactManager.addNewContact(NULL_STRING, NULL_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContactsWithEmptyName () {
        emptyContactManager.addNewContact(EMPTY_STRING, CONTACT_1_NOTES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContactsWithEmptyNotes () {
        emptyContactManager.addNewContact(CONTACT_1_NAME, EMPTY_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContactsWithEmptyNameAndNotes () {
        emptyContactManager.addNewContact(EMPTY_STRING, EMPTY_STRING);
    }

}
