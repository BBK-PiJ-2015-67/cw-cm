package test;

import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;
import spec.Meeting;
import spec.PastMeeting;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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

    private ContactManager contactsCM;
    private ContactManager emptyCM;

    @Before
    public void setUp() {
        deleteDataFile();
        emptyCM = new ContactManagerImpl();
        contactsCM = new ContactManagerImpl();
        addTestContacts(contactsCM);
    }

    @After
    public void tearDown() {
        deleteDataFile();
    }

    /* =================== FLUSH, ETC... =================== */

    @Test
    public void testContactManagerShouldHaveFileAfterFlush() {
        contactsCM.flush();
        Path p = FileSystems.getDefault().getPath(FILENAME);
        assertTrue(Files.exists(p));
    }

    @Test
    public void testEmptyCMShouldHaveNoContactsAfterFlush() {
        emptyCM.flush();
        emptyCM = new ContactManagerImpl();
        Set<Contact> contacts = emptyCM.getContacts(EMPTY_STRING);
        assertTrue(contacts.isEmpty());
    }

    @Test
    public void testEmptyCMShouldHaveNoMeetingsAfterFlush() {
        emptyCM.flush();
        emptyCM = new ContactManagerImpl();
        Meeting mtg = emptyCM.getMeeting(ONE);
        assertNull(mtg);
    }

    @Test
    public void testContactManagerShouldHaveAddedDefaultContactsAfterFlush() {
        contactsCM.flush();

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
    public void testContactManagerShouldHaveAddedMeetingsAfterFlush() {
        Set<Contact> contacts = contactsCM.getContacts(CONTACT_1_ID, CONTACT_2_ID);

        contactsCM.addFutureMeeting(contacts, new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY));
        contactsCM.addNewPastMeeting(contacts, new GregorianCalendar(PAST_YEAR, PAST_MONTH, PAST_DAY), MEETING_NOTES);

        Meeting m1 = contactsCM.getFutureMeeting(FIRST_MEETING_ID);
        PastMeeting m2 = contactsCM.getPastMeeting(SECOND_MEETING_ID);

        contactsCM.flush();

        ContactManager cm2 = new ContactManagerImpl();

        Meeting m1b = cm2.getFutureMeeting(FIRST_MEETING_ID);
        PastMeeting m2b = cm2.getPastMeeting(SECOND_MEETING_ID);

        assertEquals(m1.getId(), m1b.getId());
        assertEquals(m1.getDate(), m1b.getDate());
        assertEquals(m2.getId(), m2b.getId());
        assertEquals(m2.getDate(), m2b.getDate());
        assertEquals(m2.getNotes(), m2b.getNotes());
    }
}
