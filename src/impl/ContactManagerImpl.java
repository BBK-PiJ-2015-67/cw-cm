package impl;

import spec.*;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A Contact Manager implementation
 *
 * <h3>Assumptions</h3>
 * <ul>
 *     <li>
 *     <strong>Date/Time:</strong> I've opted to update the application's
 *     internal calendar object whenever a date comparison is needed.
 *     eg. {@code addFutureMeeting()}, {@code addNewPastMeeting()},
 *     {@code addMeetingNotes()}<br>
 *     This will only affect edge cases but it is worth noting.
 *     <br>
 *     For example, calling {@code addNewPastMeeting()} with a newly
 *     instantiated Calendar object set to "now" will succeed as the time elapsed
 *     between the method being called and its execution will mean that the
 *     meeting is now in the past.
 *     </li>
 *     <li>
 *     <strong>Meeting/Contact IDs:</strong> The CM is limited to
 *     2^31 - 1 meetings/contacts due to the spec calling for int as the ID type.<br>
 *     Ideally we should use a UUID &ndash; or we should let a database handle it.
 *     </li>
 * </ul>
 *
 * @see ContactManager
 *
 * @author lmignot
 */
public class ContactManagerImpl implements ContactManager {

    private static final String FILENAME = "contacts.txt";
    private Calendar cmDate;
    private Set<Contact> cmContacts;
    private List<Meeting> cmMeetings;
    private int nextMeetingId;
    private int nextContactId;

    public ContactManagerImpl () {
        readDataFromFile();
        cmDate = new GregorianCalendar();
    }

    /**
     * @see ContactManager#addFutureMeeting(Set, Calendar)
     * @throws IllegalArgumentException if the meeting is set for a time in the past,
     *                                  or if any contact is unknown / non-existent
     * @throws NullPointerException if the meeting or the date are null;
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        Objects.requireNonNull(contacts);
        Objects.requireNonNull(date);

        // update the internal clock before any date comparison
        cmDate = new GregorianCalendar();

        if (!date.after(cmDate) || !isValidContacts(contacts)) {
            throw new IllegalArgumentException();
        }

        int id = nextMeetingId;
        cmMeetings.add(new FutureMeetingImpl(id, date, contacts));
        nextMeetingId++;
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
                throw new IllegalStateException();
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
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * @see ContactManager#getMeeting(int)
     */
    @Override
    public Meeting getMeeting(int id) {
        try {
            return cmMeetings.stream().filter(mtg -> mtg.getId() == id).findFirst().get();
        } catch (NoSuchElementException noSuchElEx) {
            return null;
        }
    }

    /**
     * @see ContactManager#getFutureMeetingList(Contact)
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        Objects.requireNonNull(contact);
        if (!isValidContact(contact)) {
            throw new IllegalArgumentException();
        }

        List<Meeting> result = new ArrayList<>();
        result.addAll(
            cmMeetings.stream()
            .filter(m -> m.getContacts().contains(contact) && m instanceof FutureMeeting)
            .sorted((m1, m2) -> (m1.getDate().compareTo(m2.getDate())))
            .distinct()
            .collect(Collectors.toList())
        );
        return result;
    }

    /**
     * @see ContactManager#getMeetingListOn(Calendar)
     * @throws NullPointerException if the date is null
     */
    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        Objects.requireNonNull(date);

        List<Meeting> result = new ArrayList<>();
        result.addAll(
            cmMeetings.stream()
            .filter(m -> m.getDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                    m.getDate().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                    m.getDate().get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH))
            .sorted((m1, m2) -> (m1.getDate().compareTo(m2.getDate())))
            .distinct()
            .collect(Collectors.toList())
        );
        return result;
    }

    /**
     * @see ContactManager#getPastMeetingListFor(Contact)
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) {
        Objects.requireNonNull(contact);
        if (!isValidContact(contact)) {
            throw new IllegalArgumentException();
        }

        List<PastMeeting> result = new ArrayList<>();
        result.addAll(
            cmMeetings.stream()
            .filter(m -> m.getContacts().contains(contact) && m instanceof PastMeeting)
            .sorted((m1, m2) -> (m1.getDate().compareTo(m2.getDate())))
            .map(m -> (PastMeeting) m)
            .distinct()
            .collect(Collectors.toList())
        );

        return result;
    }

    /**
     * @see ContactManager#addNewPastMeeting(Set, Calendar, String)
     * @throws IllegalArgumentException if the messages are empty or the date
     *                                  provided is NOT in the past
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        Objects.requireNonNull(contacts);
        Objects.requireNonNull(date);
        Objects.requireNonNull(text);

        // update the internal clock before any date comparison
        cmDate = new GregorianCalendar();

        if (!date.before(cmDate) || text.equals("") || !isValidContacts(contacts)) {
            throw new IllegalArgumentException();
        }

        int id = nextMeetingId;
        cmMeetings.add(new PastMeetingImpl(id, date, contacts, text));
        nextMeetingId++;
    }

    /**
     * @see ContactManager#addMeetingNotes(int, String)
     * @throws IllegalArgumentException if the meeting does not exist
     * @throws IllegalStateException if the meeting is set for a date in the future
     * @throws NullPointerException if the notes are null
     */
    @Override
    public void addMeetingNotes(int id, String text) {
        Objects.requireNonNull(text);

        // update the internal clock before any date comparison
        cmDate = new GregorianCalendar();

        Meeting mtg = getMeeting(id);
        if (mtg == null) { throw new IllegalArgumentException(); }
        if (mtg.getDate().after(cmDate)) { throw new IllegalStateException(); }

        StringJoiner sj = new StringJoiner("\n");
        if (mtg instanceof PastMeeting && !((PastMeeting) mtg).getNotes().equals("")) {
            sj.add(((PastMeeting) mtg).getNotes());
        }
        sj.add(text);

        PastMeeting newMeeting = new PastMeetingImpl(id, mtg.getDate(), mtg.getContacts(), sj.toString());
        cmMeetings.set(id - 1, newMeeting);
    }

    /**
     * @see ContactManager#addNewContact(String, String)
     * @throws IllegalArgumentException if the name or the notes are empty strings
     * @throws NullPointerException if the name or the notes are null
     */
    @Override
    public int addNewContact(String name, String notes) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(notes);
        if (name.equals("") || notes.equals("")) {
            throw new IllegalArgumentException();
        }
        int id = nextContactId;
        cmContacts.add(new ContactImpl(id, name, notes));
        nextContactId++;
        return id;
    }

    /**
     * As per spec, this method can be used to retrieve the
     * complete set of contacts.<br>
     * This implementation returns a copy of the set rather
     * than a reference to the internal set.
     *
     * @see ContactManager#getContacts(String)
     * @throws NullPointerException if the parameter is null
     */
    @Override
    public Set<Contact> getContacts(String name) {
        Objects.requireNonNull(name);
        if(name.equals("")) { return cloneContacts(); }

        Set<Contact> result = new HashSet<>();
        result.addAll(
            cmContacts
                .stream()
                .filter(c -> c.getName().contains(name))
                .collect(Collectors.toSet())
        );
        return result;
    }

    /**
     * @see ContactManager#getContacts(int...)
     * @throws IllegalArgumentException if no IDs are provided or if any of the provided
     *                                  IDs does not correspond to a real contact
     * @throws NullPointerException if the argument is null
     */
    @Override
    public Set<Contact> getContacts (int... ids) {
        Objects.requireNonNull(ids);
        if (!isValidContactIds(ids)) { throw new IllegalArgumentException(); }

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

    /**
     * @see ContactManager#flush()
     */
    @Override
    public void flush() {
        try {
            Files.deleteIfExists(FileSystems.getDefault().getPath(FILENAME));
        } catch(IOException ioEx) {
            ioEx.printStackTrace();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(
            new BufferedOutputStream(
                new FileOutputStream(FILENAME)
            )
        )) {
            out.writeObject(cmContacts);
            out.writeObject(cmMeetings);
            out.writeObject(nextMeetingId);
            out.writeObject(nextContactId);
        } catch (FileNotFoundException nfEx) {
            nfEx.printStackTrace();
        } catch (AccessDeniedException secEx) {
            secEx.printStackTrace();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    /**
     * Reads ContactManager data from a file if it exists.<br>
     * The file is assumed to be in the same directory as the class
     * and named {@code contacts.txt}<br>
     * If the file does not exist, initialises ContactManager with empty
     * data structures and starting Contact/Meeting ids of 1
     */
    private void readDataFromFile() {
        Set<Contact> tmpContacts = null;
        List<Meeting> tmpMeetings = null;
        int tmpNextMeetingId = -1;
        int tmpNextContactId = -1;

        if(Files.exists(FileSystems.getDefault().getPath(FILENAME))) {
            try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(
                    new FileInputStream(FILENAME)
                )
            )) {
                tmpContacts = (Set<Contact>) in.readObject();
                tmpMeetings = (List<Meeting>) in.readObject();
                tmpNextMeetingId = (int) in.readObject();
                tmpNextContactId = (int) in.readObject();
            } catch (ClassNotFoundException | IOException ex) {
                ex.printStackTrace();
            }
        }

        cmContacts = (tmpContacts == null) ? new HashSet<>() : tmpContacts;
        cmMeetings = (tmpMeetings == null) ? new ArrayList<>() : tmpMeetings;
        nextMeetingId = (tmpNextMeetingId == -1) ? 1 : tmpNextMeetingId;
        nextContactId = (tmpNextContactId == -1) ? 1 : tmpNextContactId;
    }

    /**
     * Helper method to check if a list of Contact ids exist in the
     * internal Contact list
     *
     * @param ids the list of 1 or more ids to search for
     * @return True if ALL the ids are found, false if ANY are NOT found
     */
    private boolean isValidContactIds(int... ids) {
        if (ids.length == 0) {
            return false;
        }
        int found = 0;
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
     * Helper method to check if a single Contact exists in the system
     * @param contact the Contact to check
     * @return true if the Contact exists, false if not
     */
    private boolean isValidContact (Contact contact) {
        return isValidContactIds((new int[1])[0] = contact.getId());
    }

    /**
     * Helper method to check if all contacts passed as arguments
     * exist in the ContactManager
     * @param contacts The Set of contacts to check
     * @return true if all contacts exist, false if any contacts do not exist
     */
    private boolean isValidContacts (Set<Contact> contacts) {
        return !contacts.isEmpty() && isValidContactIds(contacts.stream().mapToInt(Contact::getId).toArray());
    }

    /**
     * Returns a clone of the internal contacts list
     * I might have to give you my contacts, but you CANNOT have my list!
     *
     * @return A copy of the Set containing all internal contacts
     */
    private Set<Contact> cloneContacts() {
        Set<Contact> clone = new HashSet<>();
        clone.addAll(cmContacts);
        return clone;
    }
}
