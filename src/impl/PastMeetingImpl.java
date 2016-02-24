package impl;

import spec.Contact;
import spec.PastMeeting;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * A Past Meeting is a meeting that has happened in the past
 * It contains notes.
 *
 * @see PastMeeting
 * @see spec.Meeting
 *
 * @author lmignot
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting, Serializable {

    private String meetingNotes;

    /**
     * @see MeetingImpl
     * @param notes The notes for the meeting
     * @throws NullPointerException if the date or contacts are null
     * @throws NullPointerException if the notes are null
     * @throws IllegalArgumentException if the id is negative or 0, or if the set of contacts is empty
     */
    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes) {
        super(id, date, contacts);
        if(notes == null) {
            throw new NullPointerException("null passed as argument to notes");
        }
        meetingNotes = notes;
    }

    /**
     * @see PastMeeting#getNotes()
     */
    @Override
    public String getNotes() {
        return meetingNotes;
    }
}
