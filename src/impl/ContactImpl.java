package impl;

import spec.Contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of Contact interface.
 *
 * @see Contact
 * @author lmignot
 */
public class ContactImpl implements Contact, Serializable {

    private final int contactId;
    private final String contactName;
    private final List<String> notes;

    /**
     * Create a new Contact without notes
     *
     * @param id The contact's ID
     * @param name The contact's name
     * @throws IllegalArgumentException if the ID is 0 or negative,
     *                                  or the name is an empty string
     * @throws NullPointerException if the name is null
     */
    public ContactImpl(int id, String name) {
        Objects.requireNonNull(name);
        if (id <= 0 || name.equals("")) {
            throw new IllegalArgumentException();
        }

        notes = new ArrayList<>();
        contactId = id;
        contactName = name;
    }

    /**
     * Create a new Contact with some notes
     * @see ContactImpl#ContactImpl(int, String)
     * @param notes Some notes about this contact
     * @throws NullPointerException if the name is null or the notes are null
     * @throws IllegalArgumentException if the ID is 0 or negative,
     *                                  or the name or notes are empty strings
     */
    public ContactImpl (int id, String name, String notes) {
        this(id, name);
        addNotes(notes);
    }

    /**
     * @see Contact#getId()
     */
    public int getId() {
        return contactId;
    }

    /**
     * @see Contact#getName()
     */
    public String getName() {
        return contactName;
    }

    /**
     * @see Contact#getNotes()
     */
    @Override
    public String getNotes() {
        return notes.stream().collect(Collectors.joining("\n"));
    }

    /**
     * @see Contact#addNotes(String)
     * @throws NullPointerException if the notes are null
     * @throws IllegalArgumentException if the notes are empty
     */
    @Override
    public void addNotes(String note) {
        Objects.requireNonNull(note);
        if (note.equals("")) {
            throw new IllegalArgumentException("Notes cannot be empty");
        }
        notes.add(note);
    }
}
