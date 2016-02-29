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

import static org.junit.Assert.assertEquals;
import static test.TestCommon.*;

/**
 * ContactManager tests
 * Split up the tests as the main test class was getting quite large
 *
 * This class tests the Flush methods in ContactManager
 *
 * @author lmignot
 */
public class ContactManagerTest {

    private ContactManager cm;

    @Before
    public void setUp() {
        TestCommon.deleteDataFile();

        cm = new ContactManagerImpl();
        cm.addNewContact(CONTACT_1_NAME, CONTACT_1_NOTES);
        cm.addNewContact(CONTACT_2_NAME, CONTACT_2_NOTES);
    }

    @After
    public void tearDown() {
        TestCommon.deleteDataFile();
        cm = null;
    }

    /* =================== FLUSH, ETC... =================== */

    @Test
    public void testContactManagerShouldHaveAddedContactsAfterFlush () {
        cm.flush();

        ContactManager cm2 = new ContactManagerImpl();

        Set<Contact> contacts = cm2.getContacts(CONTACT_1_ID, CONTACT_2_ID);

        Contact c1 = contacts.stream().filter(c -> c.getId() == CONTACT_1_ID).findFirst().get();
        Contact c2 = contacts.stream().filter(c -> c.getId() == CONTACT_2_ID).findFirst().get();

        assertEquals(c1.getName(), CONTACT_1_NAME);
        assertEquals(c1.getNotes(), CONTACT_1_NOTES);
        assertEquals(c2.getName(), CONTACT_2_NAME);
        assertEquals(c2.getNotes(), CONTACT_2_NOTES);
    }

    @Test
    public void testContactManagerShouldHaveAddedMeetingsAfterFlush () {
        Set<Contact> contacts = cm.getContacts(CONTACT_1_ID, CONTACT_2_ID);

        cm.addFutureMeeting(contacts, new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY));
        cm.addNewPastMeeting(contacts, new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY), MEETING_NOTES_1);

        Meeting m1 = cm.getFutureMeeting(1);
        PastMeeting m2 = cm.getPastMeeting(2);

        cm.flush();

        ContactManager cm2 = new ContactManagerImpl();

        Meeting m1b = cm2.getFutureMeeting(1);
        PastMeeting m2b = cm2.getPastMeeting(2);

        assertEquals(m1.getId(), m1b.getId());
        assertEquals(m1.getDate(), m1b.getDate());
        assertEquals(m2.getId(), m2b.getId());
        assertEquals(m2.getDate(), m2b.getDate());
        assertEquals(m2.getNotes(), m2b.getNotes());
    }
}
