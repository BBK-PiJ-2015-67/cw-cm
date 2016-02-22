package impl;

import spec.*;

import java.util.*;

/**
 * A Contact Manager implementation
 *
 * I've made a few assumptions:
 *
 * <ul>
 *     <li><strong>Date/Time:</strong> As per the Java documentation, calling getTime() on a Date/Calendar
 *     object triggers a time update on the object &ndash; I've implemented adding Past/Future Meetings with
 *     this in mind.<br>
 *     A side-effect is that calling addFutureMeeting() with a newly instantiated Calendar object
 *     with no specific future time will result in failure as the time elapsed between the method
 *     being called and its execution <em>may</em> mean that the meeting is no longer in the future.</li>
 *     <li><strong>Contact IDs, Meeting IDs:</strong>
 *     If there was a method to remove contacts or meetings from this application
 *     then using the size of the data structures would be a bad idea &ndash; if a contact was removed
 *     from the middle of the set then the ID would be greater than the size of the set, hence we could
 *     provide a non-unique ID which would be invalid.<br>
 *     Contact removal has not been specified however so I have opted to use this to avoid an additional loop.<br>
 *     Ideally we would use a UUID or GUID or some other mechanism but the return type has been specified as
 *     int &ndash; I considered using a hash on the contacts but there is no guarantee a contact would not
 *     have the same name as another contact so hashing the name would not guarantee a unique ID &ndash;
 *     we would require a further identifier to ensure a unique hash</li>
 * </ul>
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
     * @throws IllegalArgumentException if the meeting is set for a time in the past,
     *                                  or if any contact is unknown / non-existent
     * @throws NullPointerException if the meeting or the date are null;
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        if (contacts == null || date == null) {
            throw new NullPointerException("null argument passed to contacts and/or date");
        }

        // update the internal clock before any date comparison
        this.now.getTime();

        if (!date.after(this.now)) {
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

    /**
     * @see ContactManager#getPastMeeting(int)
     * @throws IllegalStateException if there is a meeting with that ID happening in the future
     */
    @Override
    public PastMeeting getPastMeeting(int id) {
        Meeting mtg = this.getMeeting(id);
        if (mtg == null) {
            return null;
        } else {
            if (mtg instanceof PastMeeting) {
                return (PastMeeting) mtg;
            } else {
                throw new IllegalStateException("id specifies a meeting that is a future meeting");
            }
        }
    }

    /**
     * @see ContactManager#getFutureMeeting(int)
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the past
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

    /**
     * @see ContactManager#getFutureMeetingList(Contact)
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        if (contact == null) {
            throw new NullPointerException("contact should not be null");
        }
        if (!this.isValidContact(contact)) {
            throw new IllegalArgumentException("specified contact does not exist");
        }

        // Use a set so we have no duplicates
        Set<Meeting> uniqueResult = new HashSet<>();

        // Really want to use streams but we haven't had them in class yet so
        // not sure it's permitted
        for(Meeting m: this.meetings) {
            if (m.getContacts().contains(contact) && m instanceof FutureMeeting) {
                uniqueResult.add(m);
            }
        }

        List<Meeting> result = new ArrayList<>();
        result.addAll(uniqueResult);

        // Sort using a lambda here
        // saves on writing a comparison method
        Collections.sort(result, (m1, m2) -> (m1.getDate().compareTo(m2.getDate())));
        return result;
    }

    /**
     * @see ContactManager#getMeetingListOn(Calendar)
     * @throws NullPointerException if the date is null
     */
    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        if (date == null) {
            throw new NullPointerException("no date provided.");
        }

        // Use a set so we have no duplicates
        Set<Meeting> uniqueResult = new HashSet<>();

        // since the method is "getMeetingListOn" it
        // seems sensible to assume we're talking about
        // a day as opposed to a specific time
        for(Meeting m: this.meetings) {
            if (m.getDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                    m.getDate().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                    m.getDate().get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
                uniqueResult.add(m);
            }
        }
        List<Meeting> result = new ArrayList<>();
        result.addAll(uniqueResult);

        // Sort using a lambda here
        // saves on writing a comparison method
        Collections.sort(result, (m1, m2) -> (m1.getDate().compareTo(m2.getDate())));
        return result;
    }

    /**
     * @see ContactManager#getPastMeetingListFor(Contact)
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) {
        if (contact == null) {
            throw new NullPointerException("contact should not be null");
        }
        if (!this.isValidContact(contact)) {
            throw new IllegalArgumentException("specified contact does not exist");
        }

        return null;
    }

    /**
     * @see ContactManager#addNewPastMeeting(Set, Calendar, String)
     * @throws IllegalArgumentException if the messages are empty or the date
     *                                  provided is NOT in the past
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        if (contacts == null || date == null || text == null) {
            throw new NullPointerException("null argument passed to contacts, date, or notes");
        }

        // update the internal clock before any date comparison
        this.now.getTime();

        if (!date.before(this.now)) {
            throw new IllegalArgumentException("a past meeting must have occurred in the past");
        }
        if (text.equals("")) {
            throw new IllegalArgumentException("messages should not be empty");
        }
        if (!this.isValidContacts(contacts)) {
            throw new IllegalArgumentException("one or more of the provided contacts do not exist");
        }

        int id = this.nextMeetingId;
        this.nextMeetingId++;

        this.meetings.add(new PastMeetingImpl(id, date, contacts, text));
    }

    @Override
    public void addMeetingNotes(int id, String text) {

    }

    /**
     * @see ContactManager#addNewContact(String, String)
     * @throws IllegalArgumentException if the name or the notes are empty strings
     * @throws NullPointerException if the name or the notes are null
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
     * @throws NullPointerException if the parameter is null
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
     * @throws IllegalArgumentException if no IDs are provided or if any of the provided
     *                                  IDs does not correspond to a real contact
     * @throws NullPointerException if the argument is null
     */
    @Override
    public Set<Contact> getContacts(int... ids) {
        if (ids == null) {
            throw new NullPointerException("null argument passed for ids");
        }
        if (ids.length == 0 || !this.isValidContactIds(ids)) {
            throw new IllegalArgumentException("one or more of the specified ids does not exist");
        }

        Set<Contact> result = new HashSet<>();

        for (Contact c : this.contacts) {
            for(int i : ids) {
                if(c.getId() == i) {
                    result.add(c);
                }
            }
        }

        return result;
    }

    @Override
    public void flush() {

    }

    /**
     * Helper method to check if a single Contact exists in the system
     * @param contact the Contact to check
     * @return true if the Contact exists, false if not
     */
    private boolean isValidContact(Contact contact) {
        boolean result = false;
        for (Contact c : this.contacts) {
            if (c.getId() == contact.getId() && c.getName().equals(contact.getName())) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Helper method to check if a list of Contact ids exist in the
     * internal Contact list
     *
     * @param ids the list of 1 or more ids to search for
     * @return True if ALL the ids are found, false if ANY are NOT found
     */
    private boolean isValidContactIds(int... ids) {
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

    /**
     * Helper method to check if all contacts passed as arguments
     * exist in the ContactManager
     * @param contacts The Set of contacts to check
     * @return true if all contacts exist, false if any contacts do not exist
     */
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

        return this.isValidContactIds(ids);
    }

    /**
     * Helper method to check if an array of int has duplicates
     * @param ids the array to check for unique values
     * @return false if all the ids are unique, true if any duplicates are found
     */
    private boolean hasDuplicateIds (int... ids) {
        // store the ids in a set - if this set is smaller
        // than the array of ids passed in the arguments
        // then a duplicate id is in the list
        Set<Integer> uniqueIds = new HashSet<>();

        for (int i: ids) {
            uniqueIds.add(i);
        }

        return (uniqueIds.size() != ids.length);
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
