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
        addTestContacts(contactsCM);
    }

    @After
    public void tearDown() {
        deleteDataFile();
    }

    /* =================== CONTACTS =================== */

    @Test
    public void testAddContactsToEmptyCM() {
        addTestContacts(cleanCM);

        assertFalse(cleanCM.getContacts(EMPTY_STRING).isEmpty());
        assertEquals(cleanCM.getContacts(EMPTY_STRING).size(), NUM_CONTACTS_DEFAULT);
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
        assertEquals(testContacts.size(), DUPLICATE_CONTACTS);

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
        assertEquals(testContacts.size(), SIMILAR_CONTACTS);
    }

    @Test
    public void testGetContactsByNameWhenCMHasNoContacts() {
        Set<Contact> testContacts = cleanCM.getContacts(CONTACT_1_NAME);

        assertTrue(testContacts.isEmpty());
    }

    @Test
    public void testGetContactsByNameWithNonExistentContact() {
        Set<Contact> testContacts = contactsCM.getContacts(NON_EXISTENT_CONTACT_NAME);

        assertTrue(testContacts.isEmpty());
    }

    @Test
    public void testGetContactsByIds() {
        Set<Contact> testContacts = contactsCM.getContacts(
            CONTACT_1_ID,
            CONTACT_2_ID
        );

        assertEquals(testContacts.size(), TWO);

        Contact c1 = testContacts.stream().filter(c -> c.getId() == CONTACT_1_ID).findFirst().orElse(NULL_CONTACT);
        Contact c2 = testContacts.stream().filter(c -> c.getId() == CONTACT_2_ID).findFirst().orElse(NULL_CONTACT);

        assertNotNull(c1);
        assertNotNull(c2);
        assertEquals(c1.getName(), CONTACT_1_NAME);
        assertEquals(c1.getNotes(), CONTACT_1_NOTES);
        assertEquals(c2.getName(), CONTACT_2_NAME);
        assertEquals(c2.getNotes(), CONTACT_2_NOTES);
    }

    @Test
    public void testGetContactsByIdsWithDuplicates() {
        Set<Contact> testContacts = contactsCM.getContacts(
            CONTACT_1_ID,
            CONTACT_2_ID,
            CONTACT_3_ID,
            CONTACT_4_ID,
            CONTACT_3_ID,
            CONTACT_1_ID,
            CONTACT_5_ID,
            CONTACT_6_ID
        );

        assertEquals(testContacts.size(), NUM_CONTACTS_DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContactsByIdsWithInvalidIds() {
        contactsCM.getContacts(
            ILLEGAL_ID_1,
            ILLEGAL_ID_2,
            ILLEGAL_ID_3,
            ILLEGAL_ID_4
        );
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsByIdsWithNull() {
        cleanCM.getContacts(NULL_INT_ARRAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContactsByIdsWhenCMHasNoContacts() {
        cleanCM.getContacts(
            CONTACT_1_ID,
            CONTACT_2_ID,
            CONTACT_3_ID,
            CONTACT_4_ID
        );
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsWithNullName() {
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
