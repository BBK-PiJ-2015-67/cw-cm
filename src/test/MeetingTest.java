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
 * As Meeting is an abstract class I've implemented a nested mock
 * class so we can test basic functionality
 *
 * @author lmignot
 */
public class MeetingTest {

    private class MeetingMock implements Meeting {

        private Calendar meetingDate;
        private int meetingId;
        private Set<Contact> meetingContacts;

        MeetingMock (int id, Calendar date, Set<Contact> contacts) {
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

    private Contact organiser;
    private Contact attendee1;
    private Contact attendee2;
    private Contact attendee3;

    @Before
    public void setUp () {
        meetingContacts = new HashSet<>();
        date = new GregorianCalendar(1979, 7, 10);

        organiser = new ContactImpl(23, "Jim Harrison");
        attendee1 = new ContactImpl(33, "Van Wrinkle");
        attendee2 = new ContactImpl(99, "Susan Doubtfire");
        attendee3 = new ContactImpl(2, "Harry Smith");

        meetingContacts.add(organiser);
        meetingContacts.add(attendee1);
        meetingContacts.add(attendee2);
        meetingContacts.add(attendee3);
    }

    @Test
    public void createAMeeting () {
        mtg = new MeetingMock(id, date, meetingContacts);

        assertThat(mtg.getDate()).isEqualTo(date);
        assertThat(mtg.getId()).isEqualTo(id);
        assertThat(mtg.getContacts()).isEqualTo(meetingContacts);
    }
}
