package test;

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

    private class MeetingMock implements Meeting {

        MeetingMock (int id, Calendar date, Set<Contact> contacts) {

        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public Calendar getDate() {
            return null;
        }

        @Override
        public Set<Contact> getContacts() {
            return null;
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
