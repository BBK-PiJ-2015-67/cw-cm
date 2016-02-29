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
import static test.TestCommon.*;

/**
 * Meeting tests<br>
 * Meeting is an abstract class so I've implemented a nested mock
 * class {@code MeetingMock} in order to test Meeting functionality
 *
 * @author lmignot
 */
public class MeetingTest {

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
        meeting = new MeetingMock(ID_NEG, meetingDate, meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroIdShouldThrow () {
        meeting = new MeetingMock(ID_ZERO, meetingDate, meetingContacts);
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
