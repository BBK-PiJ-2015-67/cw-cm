package test;

import impl.ContactImpl;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.Meeting;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Meeting tests<br>
 * Meeting is an abstract class so I've implemented a nested mock
 * class {@code MeetingMock} in order to test Meeting functionality
 *
 * @author lmignot
 */
public class MeetingTest {

    private static final int CONTACT_1_ID = 1;
    private static final int CONTACT_2_ID = 2;
    private static final int CONTACT_3_ID = 3;
    private static final int CONTACT_4_ID = 4;
    private static final String CONTACT_1_NAME = "Wade Wilson";
    private static final String CONTACT_2_NAME = "Peter Quill";
    private static final String CONTACT_3_NAME = "Jessica Jones";
    private static final String CONTACT_4_NAME = "Bruce Wayne";

    private static final int YEAR = 1967;
    private static final int MONTH = 4;
    private static final int DAY = 11;
    private static final int MEETING_ID = 23;
    private static final int MEETING_ID_NEG = -98;
    private static final int MEETING_ID_ZERO = 0;
    private static final int EXPECTED_CONTACT_SET_SIZE = 4;

    // for error checking
    private static final Calendar NULL_CAL = null;
    private static final Set<Contact> NULL_CONTACTS = null;
    private static final Set<Contact> EMPTY_CONTACTS = new HashSet<>();

    private Meeting meeting;
    private Set<Contact> meetingContacts;
    private Calendar meetingDate;

    @Before
    public void setUp () {
        meetingDate = new GregorianCalendar(YEAR, MONTH, DAY);
        meetingContacts = new HashSet<>();

        meetingContacts.add(new ContactImpl(CONTACT_1_ID, CONTACT_1_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_2_ID, CONTACT_2_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_3_ID, CONTACT_3_NAME));
        meetingContacts.add(new ContactImpl(CONTACT_4_ID, CONTACT_4_NAME));
    }

    @Test
    public void createAMeeting () {
        meeting = new MeetingMock(MEETING_ID, meetingDate, meetingContacts);

        assertNotNull(meeting);
        assertEquals(meeting.getDate(), meetingDate);
        assertEquals(meeting.getId(), MEETING_ID);
        assertEquals(meeting.getContacts(), meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdShouldThrow () {
        meeting = new MeetingMock(MEETING_ID_NEG, meetingDate, meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroIdShouldThrow () {
        meeting = new MeetingMock(MEETING_ID_ZERO, meetingDate, meetingContacts);
    }

    @Test(expected = NullPointerException.class)
    public void nullDateShouldThrow () {
        meeting = new MeetingMock(MEETING_ID, NULL_CAL, meetingContacts);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsShouldThrow () {
        meeting = new MeetingMock(MEETING_ID, meetingDate, NULL_CONTACTS);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateShouldThrow () {
        meeting = new MeetingMock(MEETING_ID, NULL_CAL, NULL_CONTACTS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyContactsShouldThrow () {
        meeting = new MeetingMock(MEETING_ID, meetingDate, EMPTY_CONTACTS);
    }

    /**
     * A Mock Meeting class implemented in order to test
     * basic Meeting Interface functionality
     */
    private class MeetingMock implements Meeting {

        private Calendar meetingDate;
        private int meetingId;
        private Set<Contact> meetingContacts;

        MeetingMock (int id, Calendar date, Set<Contact> contacts) {
            if (id <= 0) {
                throw new IllegalArgumentException("IDs must be greater than 0");
            }
            if (date == null || contacts == null) {
                throw new NullPointerException("null passed as argument to date, contacts, or both");
            }
            if (contacts.isEmpty()) {
                throw new IllegalArgumentException("No contacts provided, cannot have a meeting without contacts.");
            }
            this.meetingId = id;
            this.meetingDate = date;
            this.meetingContacts = contacts;
        }

        @Override
        public int getId() {
            return this.meetingId;
        }

        @Override
        public Calendar getDate() {
            return this.meetingDate;
        }

        @Override
        public Set<Contact> getContacts() {
            return this.meetingContacts;
        }
    }
}
