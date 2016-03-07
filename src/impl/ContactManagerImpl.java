package impl;

import spec.*;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static impl.ContactManagerHelpers.*;
import static java.util.Objects.requireNonNull;

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
 *     instantiated Calendar object set to "now" will succeed as the
 *     time elapsed between the method being called and its execution
 *     will mean that the meeting is now in the past.
 *     </li>
 *     <li>
 *     <strong>Meeting &amp; Contact IDs:</strong> The CM is limited to
 *     2<sup>31</sup> - 1 meetings &amp; contacts due to the spec
 *     calling for int as the ID type.<br>
 *     Ideally we'd use some sort of UNIQUE IDENTIFIER such as UUID,
 *     or we'd let a database handle it.
 *     </li>
 * </ul>
 *
 * @see ContactManager
 *
 * @author lmignot
 */
public class ContactManagerImpl implements ContactManager {

    private static final String FILENAME = "contacts.txt";

    private final Set<Contact> cmContacts;
    private final List<Meeting> cmMeetings;

    private Calendar cmDate;
    private int nextMeetingId;
    private int nextContactId;

    /**
     * As per the specification a ContactManager has one
     * constructor with no argument. On instantiation, we check
     * if a data file exists, if it does we attempt to read in
     * the contents of meetings, contacts and the next ID for
     * new contacts &amp; meetings.<br>
     * If there is no file, or there's an error reading the file
     * we initialise a new CM with default values and empty data
     * structures.
     */
    public ContactManagerImpl() {
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

        cmDate = new GregorianCalendar();
        cmContacts = (tmpContacts == null) ? new HashSet<>() : tmpContacts;
        cmMeetings = (tmpMeetings == null) ? new ArrayList<>() : tmpMeetings;
        nextMeetingId = (tmpNextMeetingId == -1) ? 1 : tmpNextMeetingId;
        nextContactId = (tmpNextContactId == -1) ? 1 : tmpNextContactId;
    }

    /**
     * @see ContactManager#addFutureMeeting(Set, Calendar)
     * @throws IllegalArgumentException if the meeting is set for a time in the past,
     *                                  or if any contact is unknown / non-existent
     * @throws NullPointerException if the meeting or the date are null;
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        requireNonNullArguments(contacts, date);

        cmDate = new GregorianCalendar();

        if (!date.after(cmDate) || !cmContacts.containsAll(contacts)) {
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
        if (mtg == null) { return null; }
        if (!(mtg instanceof PastMeeting)) {
            throw new IllegalStateException();
        }
        return (PastMeeting) mtg;
    }

    /**
     * @see ContactManager#getFutureMeeting(int)
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the past
     */
    @Override
    public FutureMeeting getFutureMeeting(int id) {
        Meeting mtg = getMeeting(id);
        if (mtg == null) { return null; }
        if (!(mtg instanceof FutureMeeting)) {
            throw new IllegalArgumentException();
        }
        return (FutureMeeting) mtg;
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
        requireNonNull(contact);
        if (!cmContacts.contains(contact)) {
            throw new IllegalArgumentException();
        }

        List<Meeting> result = cmMeetings.stream()
            .filter(m -> m.getContacts().contains(contact) && m instanceof FutureMeeting)
            .sorted(Comparator.comparing(Meeting::getDate))
            .collect(Collectors.toList());
        
        return getDistinctMeetings(result);
    }

    /**
     * @see ContactManager#getMeetingListOn(Calendar)
     * @throws NullPointerException if the date is null
     */
    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        requireNonNull(date);

        List<Meeting> result = cmMeetings.stream()
            .filter(m -> m.getDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                    m.getDate().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                    m.getDate().get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH))
            .sorted(Comparator.comparing(Meeting::getDate))
            .collect(Collectors.toList());

        return getDistinctMeetings(result);
    }

    /**
     * @see ContactManager#getPastMeetingListFor(Contact)
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) {
        requireNonNull(contact);
        if (!cmContacts.contains(contact)) {
            throw new IllegalArgumentException();
        }

        List<Meeting> result = cmMeetings.stream()
            .filter(m -> m.getContacts().contains(contact) && m instanceof PastMeeting)
            .sorted(Comparator.comparing(Meeting::getDate))
            .collect(Collectors.toList());

        return getDistinctMeetings(result).stream().map(m -> (PastMeeting) m).collect(Collectors.toList());
    }

    /**
     * @see ContactManager#addNewPastMeeting(Set, Calendar, String)
     * @throws IllegalArgumentException if the messages are empty or the date
     *                                  provided is NOT in the past
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        requireNonNullArguments(contacts,date,text);

        cmDate = new GregorianCalendar();

        if (!date.before(cmDate) || text.equals("") || !cmContacts.containsAll(contacts)) {
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
        requireNonNull(text);

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
        requireNonNullArguments(name, notes);
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
     * complete set of contacts, or a specific contact by name.<br>
     * This implementation returns a copy of the set rather
     * than a reference to the internal set.
     *
     * @see ContactManager#getContacts(String)
     * @throws NullPointerException if the parameter is null
     */
    @Override
    public Set<Contact> getContacts(String name) {
        requireNonNull(name);

        if(name.equals("")) {
            return cmContacts.stream().collect(Collectors.toSet());
        }
        return cmContacts.stream()
            .filter(c -> c.getName().contains(name))
            .collect(Collectors.toSet());
    }

    /**
     * @see ContactManager#getContacts(int...)
     * @throws IllegalArgumentException if no IDs are provided or if any of the provided
     *                                  IDs does not correspond to a real contact
     * @throws NullPointerException if the argument is null
     */
    @Override
    public Set<Contact> getContacts(int... ids) {
        requireNonNull(ids);

        Set<Contact> result = cmContacts.stream()
            .filter(c -> Arrays.stream(ids).anyMatch(i -> i == c.getId()))
            .collect(Collectors.toSet());

        if (result.size() == 0) {
            throw new IllegalArgumentException();
        } else {
            return result;
        }
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
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
