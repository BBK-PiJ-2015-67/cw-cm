package impl;

import spec.Contact;
import spec.Meeting;

import java.io.Serializable;
import java.util.Calendar;
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
    public MeetingImpl (int id, Calendar date, Set<Contact> contacts) {
        if (id <= 0) {
            throw new IllegalArgumentException("IDs must be greater than 0");
        }
        if (date == null || contacts == null) {
            throw new NullPointerException("null passed as argument to date, contacts, or both");
        }
        if (contacts.isEmpty()) {
            throw new IllegalArgumentException("No contacts provided, cannot have a meeting without contacts.");
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
}
