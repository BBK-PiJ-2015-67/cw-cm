package test;

import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;
import spec.Meeting;
import spec.PastMeeting;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private static final String FILENAME = "contacts.txt";

    @Before
    public void setUp() {
        cMgr = new ContactManagerImpl();
    }

    @After
    public void tearDown() {
        cMgr = null;
        // delete the saved file between tests if possible
        try {
            Path p = FileSystems.getDefault().getPath(FILENAME);
            if (Files.exists(p)) {
                Files.delete(p);
            }
        } catch (SecurityException secex) {
            System.err.println("Permission denied while attempting to delete " + FILENAME);
            secex.printStackTrace();
        } catch (IOException ioex) {
            System.err.println("I/O error occurred while attempting to delete " + FILENAME);
            ioex.printStackTrace();
        }
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
        assertEquals(contacts2, contacts2b);
        assertEquals(contacts3, contacts3b);
        assertEquals(contacts4, contacts4b);

        Meeting m1b = cm.getFutureMeeting(1);
        Meeting m2b = cm.getFutureMeeting(2);
        PastMeeting m3b = cm.getPastMeeting(3);

        // TODO: proper comparison as these will be new objects (i think?)
        assertEquals(m1, m1b);
        assertEquals(m2, m2b);
        assertEquals(m3, m3b);
    }
}
