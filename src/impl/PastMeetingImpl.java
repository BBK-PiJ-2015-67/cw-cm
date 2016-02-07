package impl;

import spec.Contact;
import spec.PastMeeting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * A Past Meeting is a meeting that has happened in the past
 * It contains notes.
 *
 * @see spec.PastMeeting
 * @see spec.Meeting
 *
 * @author lmignot
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {


    private List<String> notes = null;

    /**
     * @see MeetingImpl
     *
     * @param notes The notes for the meeting
     * @throws NullPointerException if the notes are null
     */
    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes) {
        super(id, date, contacts);
        if(notes == null) {
            throw new NullPointerException("null passed as argument to notes");
        }
        this.notes = new ArrayList<>();
        this.notes.add(notes);
    }

    /**
     * @see PastMeeting#getNotes()
     */
    @Override
    public String getNotes() {
        String result = "";

        for(String s : this.notes) {
            result += s;
        }
        return result;
    }
}
