package impl;

import spec.*;

import java.util.*;

/**
 * A Contact Manager implementation
 *
 * <strong>Assumptions</strong>
 * <ul>
 *     <li><strong>Date/Time:</strong> As per Java docs, calling getTime() on a Date/Calendar
 *     object triggers a time update &ndash; I've implemented adding Past/Future Meetings with
 *     this in mind.<br>
 *     A side-effect is that calling addFutureMeeting() with a newly instantiated Calendar object
 *     with no specific future time will result in failure as the time elapsed between the method
 *     being called and its execution <em>may</em> mean that the meeting is no longer in the future.</li>
 * </ul>
 *
 * @see ContactManager
 *
 * @author lmignot
 */
public class ContactManagerImpl implements ContactManager {

    private final Set<Contact> cmContacts;
    private final List<Meeting> meetings;
    private final Calendar cmDate;
    private int nextMeetingId;
    private int nextUserId;

    public ContactManagerImpl () {
        cmContacts = new HashSet<>();
        meetings = new ArrayList<>();
        cmDate = new GregorianCalendar();
        nextMeetingId = 1;
        nextUserId = 1;
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
        cmDate.getTime();

        if (!date.after(cmDate)) {
            throw new IllegalArgumentException("a future meeting must be set in the future");
        }
        if (!isValidContacts(contacts)) {
            throw new IllegalArgumentException("one or more of the provided contacts do not exist");
        }

        int id = nextMeetingId;
        nextMeetingId++;

        meetings.add(new FutureMeetingImpl(id, date, contacts));

        return id;
    }

    /**
     * @see ContactManager#getPastMeeting(int)
     * @throws IllegalStateException if there is a meeting with that ID happening in the future
     */
    @Override
    public PastMeeting getPastMeeting(int id) {
        Meeting mtg = getMeeting(id);
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
        Meeting mtg = getMeeting(id);
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
        for (Meeting m: meetings) {
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
        if (!isValidContact(contact)) {
            throw new IllegalArgumentException("specified contact does not exist");
        }

        // Use a set so we have no duplicates
        Set<Meeting> uniqueResult = new HashSet<>();

        // Really want to use streams but we haven't had them in class yet so
        // not sure it's permitted
        for(Meeting m: meetings) {
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
        for(Meeting m : meetings) {
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
        // TODO: implement this with streams maybe?
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
        if (!isValidContact(contact)) {
            throw new IllegalArgumentException("specified contact does not exist");
        }

        // Use a set so we have no duplicates
        Set<PastMeeting> uniqueResult = new HashSet<>();

        // Really want to use streams but we haven't had them in class yet so
        // not sure it's permitted
        for(Meeting m: meetings) {
            if (m.getContacts().contains(contact) && m instanceof PastMeeting) {
                uniqueResult.add((PastMeeting) m);
            }
        }

        List<PastMeeting> result = new ArrayList<>();
        result.addAll(uniqueResult);

        // Sort using a lambda here
        // saves on writing a comparison method
        Collections.sort(result, (m1, m2) -> (m1.getDate().compareTo(m2.getDate())));
        return result;
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
        cmDate.getTime();

        if (!date.before(cmDate)) {
            throw new IllegalArgumentException("a past meeting must have occurred in the past");
        }
        if (text.equals("")) {
            throw new IllegalArgumentException("messages should not be empty");
        }
        if (!isValidContacts(contacts)) {
            throw new IllegalArgumentException("one or more of the provided contacts do not exist");
        }

        int id = nextMeetingId;
        nextMeetingId++;

        meetings.add(new PastMeetingImpl(id, date, contacts, text));
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
        int id = nextUserId;

        cmContacts.add(new ContactImpl(id, name, notes));

        nextUserId++;

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

        for(Contact c : cmContacts) {
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
        if (ids.length == 0 || !isValidContactIds(ids)) {
            throw new IllegalArgumentException("one or more of the specified ids does not exist");
        }

        Set<Contact> result = new HashSet<>();

        for (Contact c : cmContacts) {
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

        for (Contact c : cmContacts) {
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

        for (Contact c : cmContacts) {
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
        int found = 0;
        for (Contact contact : contacts) {
            if (isValidContact(contact)) {
                found++;
            }
        }

        return found == contacts.size();
    }

    /**
     * Returns a clone of the internal contacts list
     *
     * @return A copy of the Set containing all internal contacts
     */
    private Set<Contact> cloneContacts() {
        Set<Contact> clone = new HashSet<>();
        clone.addAll(cmContacts);
        return clone;
    }
}
