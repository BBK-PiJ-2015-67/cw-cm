package impl;

import spec.Contact;
import spec.Meeting;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of a Meeting
 * @see Meeting
 *
 * @author lmignot
 */
public abstract class MeetingImpl implements Meeting, Serializable {

    private final Calendar meetingDate;
    private final int meetingId;
    private final Set<Contact> meetingContacts;

    /**
     * Creates a Meeting
     *
     * Extending classes should implement the same argument checks
     *
     * @param id A unique positive ID &ndash; this class does not check for uniqueness
     * @param date The meeting's date
     * @param contacts The contacts attending this meeting. Should not be empty
     *
     * @throws NullPointerException if the date or contacts are null
     * @throws IllegalArgumentException if the id is negative or 0,
     *                                  or if the set of contacts is empty
     */
    public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        Objects.requireNonNull(date);
        Objects.requireNonNull(contacts);
        if (id <= 0 || contacts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        meetingId = id;
        meetingDate = date;
        meetingContacts = contacts;
    }

    /**
     * @see Meeting#getId()
     */
    @Override
    public int getId() {
        return meetingId;
    }

    /**
     * @see Meeting#getDate()
     */
    @Override
    public Calendar getDate() {
        return meetingDate;
    }

    /**
     * @see Meeting#getContacts()
     */
    @Override
    public Set<Contact> getContacts() {
        return meetingContacts;
    }

    /**
     * Override {@code java.lang.Object.hashCode()}<br>
     * This is required in order to be able to use
     * {@code distinct()} in the Java 8 streams api.<br>
     *
     * @return the hashCode
     */
    @Override
    public int hashCode() {
        return meetingDate.hashCode() + meetingContacts.hashCode();
    }

    /**
     * Override {@code java.lang.Object.equals()}<br>
     * For our purposes a Meeting is equal if:
     * <ul>
     *     <li>It occurs on the same date as another meeting</li>
     *     <li>Both meetings contain ALL the same contacts</li>
     * </ul>
     *
     * @param other The object to compare this Meeting with
     * @return True if the Meetings are the same, false if not
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Meeting) {
            Meeting tmp = (Meeting) other;
            return tmp.getDate().compareTo(getDate()) == 0 &&
                    tmp.getContacts().containsAll(getContacts()) &&
                    getContacts().containsAll(tmp.getContacts());
        }
        return false;
    }
}
