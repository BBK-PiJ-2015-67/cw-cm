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
 * FutureMeeting tests
 *
 * @author lmignot
 */
public class FutureMeetingTest {

    private Meeting mtg;
    private int id = 48;
    private Set<Contact> meetingContacts;
    private Calendar date;

    @Before
    public void setUp () {
        meetingContacts = new HashSet<>();
        date = new GregorianCalendar(2020, 2, 29, 6, 59, 0);

        meetingContacts.add(new ContactImpl(23, "Jim Harrison"));
        meetingContacts.add(new ContactImpl(33, "Van Wrinkle"));
        meetingContacts.add(new ContactImpl(99, "Susan Doubtfire"));
        meetingContacts.add(new ContactImpl(2, "Harry Smith"));
    }

    @Test
    public void createAFutureMeeting() {
        Meeting fMtg = new FutureMeetingImpl(id, date, meetingContacts);

        assertThat(fMtg).isNotNull();
        assertThat(fMtg.getId()).isEqualTo(48);
        assertThat(fMtg.getDate()).isEqualTo(new GregorianCalendar(2020, 2, 29, 6, 59, 0));
        assertThat(fMtg.getContacts()).isNotEmpty();
        assertThat(fMtg.getContacts().size()).isEqualTo(4);
        assertThat(fMtg.getContacts()).isEqualTo(meetingContacts);
    }
}
