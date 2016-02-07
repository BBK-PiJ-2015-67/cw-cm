package impl;

import spec.Contact;
import spec.Meeting;

import java.util.Calendar;
import java.util.Set;

/**
 * Implementation of a Meeting
 * @see spec.Meeting
 *
 * @author lmignot
 */
public abstract class MeetingImpl implements Meeting {

    private Calendar meetingDate;
    private int meetingId;
    private Set<Contact> meetingContacts;

    public MeetingImpl (int id, Calendar date, Set<Contact> contacts) {
        this.meetingId = id;
        this.meetingDate = date;
        this.meetingContacts = contacts;
    }

    /**
     * @see Meeting#getId()
     */
    @Override
    public int getId() {
        return this.meetingId;
    }

    /**
     * @see Meeting#getDate()
     */
    @Override
    public Calendar getDate() {
        return this.meetingDate;
    }

    /**
     * @see Meeting#getContacts() 
     */
    @Override
    public Set<Contact> getContacts() {
        return this.meetingContacts;
    }
}
