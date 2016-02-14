package impl;

import spec.*;

import java.util.*;

/**
 * A Contact Manager implementation
 *
 * @see spec.ContactManager
 *
 * @author lmignot
 */
public class ContactManagerImpl implements ContactManager {

    private Set<Contact> contacts;
    private List<Meeting> meetings;
    private Calendar now;
    private int nextMeetingId;

    public ContactManagerImpl () {
        this.contacts = new HashSet<>();
        this.meetings = new ArrayList<>();
        this.now = new GregorianCalendar();
        this.nextMeetingId = 1;
    }

    /**
     * @see ContactManager#addFutureMeeting(Set, Calendar)
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        if (contacts == null || date == null) {
            throw new NullPointerException("null argument passed to contacts and/or date");
        }
        if (date.compareTo(this.now) <= 0) {
            throw new IllegalArgumentException("a future meeting must be set in the future");
        }
        if (!this.isValidContacts(contacts)) {
            throw new IllegalArgumentException("one or more of the provided contacts do not exist");
        }

        int id = this.nextMeetingId;
        this.nextMeetingId++;

        this.meetings.add(new FutureMeetingImpl(id, date, contacts));

        return id;
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        return null;
    }

    /**
     * @see ContactManager#getFutureMeeting(int)
     */
    @Override
    public FutureMeeting getFutureMeeting(int id) {
        Meeting mtg = this.getMeeting(id);
        if (mtg == null) {
            return null;
        } else {
            if (mtg instanceof FutureMeeting) {
                return (FutureMeeting) mtg;
            } else {
                throw new IllegalArgumentException("id specifies a meeting that occurred in the past");
            }
        }
    }

    /**
     * @see ContactManager#getMeeting(int)
     */
    @Override
    public Meeting getMeeting(int id) {
        for (Meeting m: this.meetings) {
            if (m.getId() == id) {
                return m;
            }
        }
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
     * However since contact removal has not been specified - have opted to use this
     * to avoid an additional loop
     * Ideally we would use a UUID or GUID - I considered using a hash on the contacts but
     * there is no guarantee a contact would not have the same name as another contact so hashing
     * the name would not guarantee a unique ID
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
        int found = 0;

        if (ids.length == 0) {
            return false;
        }

        for (Contact c : this.contacts) {
            for (int i : ids) {
                if (c.getId() == i) {
                    found++;
                }
            }
        }

        return (found == ids.length);
    }

    private boolean isValidContacts(Set<Contact> contacts) {
        if (contacts.isEmpty()) {
            return false;
        }
        int[] ids = new int[contacts.size()];
        int i = 0;
        for (Contact c: contacts) {
            ids[i] = c.getId();
            i++;
        }

        return this.isValidIds(ids);
    }

    /**
     * Returns a clone of the internal contacts list
     *
     * @return A copy of the Set containing all internal contacts
     */
    private Set<Contact> cloneContacts() {
        Set<Contact> clone = new HashSet<>();
        clone.addAll(this.contacts);

        return clone;
    }
}
