package test;

import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;

import java.util.Calendar;
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
        futureDate = new GregorianCalendar(2017, 3, 27);
        pastDate = new GregorianCalendar(2014, 4, 20);

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

}
