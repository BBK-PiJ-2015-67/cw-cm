package test;

import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;

import java.util.Set;

import static org.junit.Assert.*;
import static test.TestCommon.*;

/**
 * ContactManager tests
 * Split up the tests as the main test class was getting quite large
 *
 * This class tests the Contact methods in ContactManager
 * @author lmignot
 */
public class ContactManagerContactsTest {

    private ContactManager cleanCM;
    private ContactManager contactsCM;

    @Before
    public void setUp() {
        deleteDataFile();
        cleanCM = new ContactManagerImpl();
        contactsCM = new ContactManagerImpl();
        addFourContactsToCm(contactsCM);
    }

    @After
    public void tearDown() {
        // delete any data file if present after running tests
        deleteDataFile();

        cleanCM = null;
        contactsCM = null;
    }

    /* =================== CONTACTS =================== */

    @Test
    public void testAddContactsToEmptyCM() {
        cleanCM.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);
        cleanCM.addNewContact(CONTACT_2_NAME, CONTACT_2_NOTES);
        cleanCM.addNewContact(CONTACT_3_NAME, CONTACT_3_NOTES);
        cleanCM.addNewContact(CONTACT_4_NAME, CONTACT_4_NOTES);

        assertFalse(cleanCM.getContacts(EMPTY_STRING).isEmpty());
        assertEquals(cleanCM.getContacts(EMPTY_STRING).size(), EXPECTED_CONTACT_SET_SIZE);
    }

    @Test
    public void testThatNewCMHasEmptyContacts() {
        assertTrue(cleanCM.getContacts(EMPTY_STRING).isEmpty());
        assertEquals(cleanCM.getContacts(EMPTY_STRING).size(), EMPTY_SIZE);
    }

    @Test
    public void testGetSpecificContactsByNameFromCM() {
        int dupContact1Id = cleanCM.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);
        int dupContact2Id = cleanCM.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);
        int dupContact3Id = cleanCM.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);

        Set<Contact> testContacts = cleanCM.getContacts(CONTACT_1_NAME);

        assertFalse(testContacts.isEmpty());
        assertEquals(testContacts.size(), 3);

        assertNotEquals(dupContact1Id, dupContact2Id);
        assertNotEquals(dupContact1Id, dupContact3Id);
        assertNotEquals(dupContact2Id, dupContact3Id);
    }

    @Test
    public void testGetContactsByNameDoesIgnoreWhitespace() {
        cleanCM.addNewContact(CONTACT_3_NAME, CONTACT_3_NOTES);
        cleanCM.addNewContact(CONTACT_3_NAME, CONTACT_3_NOTES);
        cleanCM.addNewContact(CONTACT_3_NAME_PREFIX, CONTACT_3_NOTES);
        cleanCM.addNewContact(CONTACT_3_NAME_SUFFIX, CONTACT_3_NOTES);
        cleanCM.addNewContact(CONTACT_3_NAME_WHITESPACE, CONTACT_3_NOTES);

        Set<Contact> testContacts = cleanCM.getContacts(CONTACT_3_NAME);

        assertFalse(testContacts.isEmpty());
        assertEquals(testContacts.size(), 4);
    }

    @Test
    public void testGetContactsStringWhenCMHasNoContacts() {
        Set<Contact> testContacts = cleanCM.getContacts(CONTACT_1_NAME);

        assertTrue(testContacts.isEmpty());
    }

    @Test
    public void testGetContactsStringWithNonExistentContact() {
        Set<Contact> testContacts = contactsCM.getContacts(NON_EXISTENT_CONTACT_NAME);

        assertTrue(testContacts.isEmpty());
    }

    @Test
    public void testGetContactsByIds() {
        Set<Contact> testContacts = contactsCM.getContacts(
            CONTACT_1_ID,
            CONTACT_2_ID,
            CONTACT_3_ID,
            CONTACT_4_ID
        );

        assertEquals(testContacts.size(), EXPECTED_CONTACT_SET_SIZE);

        Contact c1 = testContacts.stream().filter(c -> c.getId() == CONTACT_1_ID).findFirst().get();
        Contact c2 = testContacts.stream().filter(c -> c.getId() == CONTACT_2_ID).findFirst().get();
        Contact c3 = testContacts.stream().filter(c -> c.getId() == CONTACT_3_ID).findFirst().get();
        Contact c4 = testContacts.stream().filter(c -> c.getId() == CONTACT_4_ID).findFirst().get();

        assertEquals(c1.getName(), CONTACT_1_NAME);
        assertEquals(c1.getNotes(), CONTACT_1_NOTES);
        assertEquals(c2.getName(), CONTACT_2_NAME);
        assertEquals(c2.getNotes(), CONTACT_2_NOTES);
        assertEquals(c3.getName(), CONTACT_3_NAME);
        assertEquals(c3.getNotes(), CONTACT_3_NOTES);
        assertEquals(c4.getName(), CONTACT_4_NAME);
        assertEquals(c4.getNotes(), CONTACT_4_NOTES);
    }

    @Test
    public void testGetContactsByIdsWithDuplicates() {
        Set<Contact> testContacts = contactsCM.getContacts(
            CONTACT_1_ID,
            CONTACT_2_ID,
            CONTACT_3_ID,
            CONTACT_4_ID,
            CONTACT_3_ID,
            CONTACT_1_ID
        );

        assertEquals(testContacts.size(), EXPECTED_CONTACT_SET_SIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContactsIdsWithInvalidIds() {
        contactsCM.getContacts(
            ILLEGAL_ID_1,
            ILLEGAL_ID_2,
            ILLEGAL_ID_3,
            ILLEGAL_ID_4
        );
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsIdsWithNull() {
        cleanCM.getContacts(NULL_INT_ARRAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContactsIdsWhenCMHasNoContacts() {
        cleanCM.getContacts(
            CONTACT_1_ID,
            CONTACT_2_ID,
            CONTACT_3_ID,
            CONTACT_4_ID
        );
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsByNameWithNull() {
        cleanCM.getContacts(NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testAddContactsWithNullName() {
        cleanCM.addNewContact(NULL_STRING, CONTACT_1_NOTES);
    }

    @Test(expected = NullPointerException.class)
    public void testAddContactsWithNullNotes() {
        cleanCM.addNewContact(CONTACT_1_NAME, NULL_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testAddContactsWithNullNameAndNotes() {
        cleanCM.addNewContact(NULL_STRING, NULL_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContactsWithEmptyName() {
        cleanCM.addNewContact(EMPTY_STRING, CONTACT_1_NOTES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContactsWithEmptyNotes() {
        cleanCM.addNewContact(CONTACT_1_NAME, EMPTY_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddContactsWithEmptyNameAndNotes() {
        cleanCM.addNewContact(EMPTY_STRING, EMPTY_STRING);
    }

}
