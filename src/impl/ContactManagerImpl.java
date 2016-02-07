package impl;

import spec.*;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * A Contact Manager implementation
 *
 * @see spec.ContactManager
 *
 * @author lmignot
 */
public class ContactManagerImpl implements ContactManager {

    public ContactManagerImpl () {}

    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        return 0;
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        return null;
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        return null;
    }

    @Override
    public Meeting getMeeting(int id) {
        return null;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        return null;
    }

    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        return null;
    }

    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) {
        return null;
    }

    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {

    }

    @Override
    public void addMeetingNotes(int id, String text) {

    }

    /**
     * @see ContactManager#addNewContact(String, String)
     */
    @Override
    public int addNewContact(String name, String notes) {
        if (name == null || notes == null) {
            throw new NullPointerException("null argument passed to name or notes");
        }
        if (name.equals("") || notes.equals("")) {
            throw new IllegalArgumentException("A contact must have a name and notes");
        }
        return 0;
    }

    @Override
    public Set<Contact> getContacts(String name) {
        return null;
    }

    @Override
    public Set<Contact> getContacts(int... ids) {
        return null;
    }

    @Override
    public void flush() {

    }
}
