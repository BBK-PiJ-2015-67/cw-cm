package test;

import impl.FutureMeetingImpl;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.FutureMeeting;
import spec.Meeting;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static test.TestCommon.*;

/**
 * FutureMeeting tests
 *
 * @author lmignot
 */
public class FutureMeetingTest {

    private Set<Contact> meetingContacts;
    private Calendar futureDate;

    @Before
    public void setUp () {
        futureDate = new GregorianCalendar(FUTURE_YEAR, FUTURE_MONTH, FUTURE_DAY);
        meetingContacts = getMeetingContacts();
    }

    @Test
    public void createAFutureMeeting() {
        Meeting mMtg = new FutureMeetingImpl(MEETING_ID, futureDate, meetingContacts);

        assertEquals(mMtg.getId(), MEETING_ID);
        assertEquals(mMtg.getDate(), futureDate);
        assertEquals(mMtg.getContacts().size(), FOUR);
        assertEquals(mMtg.getContacts(), meetingContacts);
    }

    @Test
    public void createAFutureMeetingOfTypeFutureMeeting() {
        FutureMeeting fMtg = new FutureMeetingImpl(MEETING_ID, futureDate, meetingContacts);

        assertEquals(fMtg.getId(), MEETING_ID);
        assertEquals(fMtg.getDate(), futureDate);
        assertEquals(fMtg.getContacts().size(), FOUR);
        assertEquals(fMtg.getContacts(), meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIdShouldThrow () {
        new FutureMeetingImpl(ID_NEG, futureDate, meetingContacts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroIdShouldThrow () {
        new FutureMeetingImpl(ZERO, futureDate, meetingContacts);
    }

    @Test(expected = NullPointerException.class)
    public void nullDateShouldThrow () {
        new FutureMeetingImpl(MEETING_ID, NULL_CAL, meetingContacts);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsShouldThrow () {
        new FutureMeetingImpl(MEETING_ID, futureDate, NULL_CONTACTS);
    }

    @Test(expected = NullPointerException.class)
    public void nullContactsAndDateShouldThrow () {
        new FutureMeetingImpl(MEETING_ID, NULL_CAL, NULL_CONTACTS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyContactsShouldThrow () {
        new FutureMeetingImpl(MEETING_ID, futureDate, EMPTY_CONTACTS);
    }
}
