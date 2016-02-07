package impl;

import spec.*;

import java.util.Calendar;
import java.util.HashSet;
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

    private Set<Contact> contacts;

    public ContactManagerImpl () {
        this.contacts = new HashSet<>();
    }

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
     * Regarding using the size of the Set of Contacts as the ID for the new Contact.<br>
     * If there was a method to remove contacts from this ContactManager
     * then this would be a bad idea - if a contact was removed from the middle
     * of the set then the ID would be greater than the size of the set, hence we would have a
     * duplicate ID which is invalid.<br>
     * However since contact removal has not been specified - have opted to use this for simplicity
     *
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
        int id = this.contacts.size() + 1;

        this.contacts.add(new ContactImpl(id, name, notes));

        return id;
    }

    /**
     * @see ContactManager#getContacts(String)
     */
    @Override
    public Set<Contact> getContacts(String name) {
        if(name == null) {
            throw new NullPointerException("null passed as argument for name");
        }
        if(name.equals("")) {
            return this.contacts;
        }
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
