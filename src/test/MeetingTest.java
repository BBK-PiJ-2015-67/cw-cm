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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Meeting tests
 *
 * @author lmignot
 */
public class MeetingTest {

    // As Meeting is an abstract class I've implemented a nested mock
    // class to test basic functionality
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

    private Meeting mtg;
    private int id = 23;
    private Set<Contact> meetingContacts;
    private Calendar date;

    @Before
    public void setUp () {
        meetingContacts = new HashSet<>();
        date = new GregorianCalendar(1979, 7, 10);

        meetingContacts.add(new ContactImpl(23, "Jim Harrison"));
        meetingContacts.add(new ContactImpl(33, "Van Wrinkle"));
        meetingContacts.add(new ContactImpl(99, "Susan Doubtfire"));
        meetingContacts.add(new ContactImpl(2, "Harry Smith"));
    }

    @Test
    public void createAMeeting () {
        mtg = new MeetingMock(id, date, meetingContacts);

        assertThat(mtg.getDate()).isEqualTo(date);
        assertThat(mtg.getId()).isEqualTo(id);
        assertThat(mtg.getContacts()).isEqualTo(meetingContacts);
        assertThat(mtg.getContacts()).isNotEmpty();
        assertThat(mtg).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdShouldThrow () {
        mtg = new MeetingMock(-2, date, meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroIdShouldThrow () {
        mtg = new MeetingMock(0, date, meetingContacts);
    }

    @Test(expected = NullPointerException.class)
    public void nullDateShouldThrow () {
        mtg = new MeetingMock(id, null, meetingContacts);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsShouldThrow () {
        mtg = new MeetingMock(id, date, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateShouldThrow () {
        mtg = new MeetingMock(id, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyContactsShouldThrow () {
        mtg = new MeetingMock(id, date, new HashSet<>());
    }
}
