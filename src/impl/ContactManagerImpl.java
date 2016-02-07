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
        // if an empty string is passed we return the full/empty set of contacts
        // in the Manager. I've opted to return a clone rather than a reference
        // to the internal set.
        if(name.equals("")) {
            return cloneContacts();
        }

        Set<Contact> result = new HashSet<>();

        for(Contact c : this.contacts) {
            if (c.getName().equals(name)) {
                result.add(new ContactImpl(c.getId(), c.getName(), c.getNotes()));
            }
        }

        return result;
    }

    /**
     * @see ContactManager#getContacts(int...)
     */
    @Override
    public Set<Contact> getContacts(int... ids) {
        if (ids == null || ids.length == 0) {
            throw new NullPointerException("null or empty list passed as argument for ids");
        }
        if (!isValidIds(ids)) {
            throw new NullPointerException("one or more of the specified ids does not exist");
        }

        Set<Contact> result = new HashSet<>();

        for (Contact c : this.contacts) {
            for(int i : ids) {
                if(c.getId() == i) {
                    result.add(new ContactImpl(c.getId(), c.getName(), c.getNotes()));
                }
            }
        }

        return result;
    }

    @Override
    public void flush() {

    }

    /**
     * Helper method to check if a list of Contact ids exist in the
     * internal Contact list
     *
     * @param ids the list of 1 or more ids to search for
     * @return True if ALL the ids are found, false if ANY are NOT found
     */
    private boolean isValidIds (int... ids) {
        boolean result = false;
        int found = 0;

        if (ids.length == 0) {
            return result;
        }

        for (Contact c : this.contacts) {
            for (int i : ids) {
                if (c.getId() == i) {
                    found++;
                }
            }
        }
        if (found == ids.length) {
            result = true;
        }

        return result;
    }

    /**
     * Return a clone of the internal contacts list
     *
     * @return A copied Set containing all the internal contacts
     */
    private Set<Contact> cloneContacts() {
        Set<Contact> clone = new HashSet<>();
        clone.addAll(this.contacts);

        return clone;
    }
}
