package test;

import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;
import spec.Meeting;
import spec.PastMeeting;

import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * ContactManager tests
 * Split up the tests as the main test class was getting quite large
 *
 * This class tests the Constructor and Flush methods in ContactManager
 *
 * @author lmignot
 */
public class ContactManagerTest {

    private ContactManager cMgr;

    @Before
    public void setUp() {
        // delete any data file if present before running tests
        TestUtils.deleteCMDataFile();

        cMgr = new ContactManagerImpl();
    }

    @After
    public void tearDown() {
        // delete any data file if present after running tests
        TestUtils.deleteCMDataFile();
        cMgr = null;
    }

    /* =================== CONSTRUCTOR, FLUSH, ETC... =================== */

    @Test
    public void testCreateAContactManager () {
        cMgr = new ContactManagerImpl();
        assertNotNull(cMgr);
    }

    @Test
    public void testCreateAUniqueContactManager () {
        ContactManager cm = new ContactManagerImpl();

        assertNotNull(cm);

        assertNotNull(cMgr);
        assertNotEquals(cMgr,cm);
    }

    @Test
    public void testCreateModifyFlushThenReadContactManager () {
        // create new shizzle
        ContactManager cm = new ContactManagerImpl();

        cm.addNewContact("Kendra Kinghorn", "Said to hold the secret to the legendary horn");
        cm.addNewContact("Ellis Pollak", "Probably a genius come to think of it");
        cm.addNewContact("Carrol Sin", "Christmas is his favourite time");
        cm.addNewContact("Norman Wiedemann", "AKA the weed slayer");
        Set<Contact> contacts2 = cm.getContacts(1,2);
        Set<Contact> contacts3 = cm.getContacts(1,2,3);
        Set<Contact> contacts4 = cm.getContacts(1,2,3,4);
        cm.addFutureMeeting(contacts2, new GregorianCalendar(2018,3,14,14,30));
        cm.addFutureMeeting(contacts3, new GregorianCalendar(2018,3,14,11,30));
        cm.addNewPastMeeting(contacts4, new GregorianCalendar(2013,3,14,12,59), "Notes");

        Meeting m1 = cm.getFutureMeeting(1);
        Meeting m2 = cm.getFutureMeeting(2);
        PastMeeting m3 = cm.getPastMeeting(3);

        // save da shizzle
        cm.flush();

        // load da shizzle
        cm = new ContactManagerImpl();

        Set<Contact> contacts2b = cm.getContacts(1,2);
        Set<Contact> contacts3b = cm.getContacts(1,2,3);
        Set<Contact> contacts4b = cm.getContacts(1,2,3,4);

        // TODO: proper comparison as these will be new objects (i think?)
        // they are new objects...
        assertFalse(contacts2b.isEmpty());
        assertFalse(contacts3b.isEmpty());
        assertFalse(contacts4b.isEmpty());

        Meeting m1b = cm.getFutureMeeting(1);
        Meeting m2b = cm.getFutureMeeting(2);
        PastMeeting m3b = cm.getPastMeeting(3);

        assertEquals(m1.getId(), m1b.getId());
        assertEquals(m2.getId(), m2b.getId());
        assertEquals(m3.getId(), m3b.getId());
    }
}
