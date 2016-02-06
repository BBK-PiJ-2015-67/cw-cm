package impl;

import spec.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Contact interface
 *
 * @see spec.Contact;
 * @author lmignot
 */
public class ContactImpl implements Contact {

    private int id;
    private String fullName;
    private List<String> notes = null;

    private final String ID_ERROR_MSG = "IDs must be greater than 0";
    private final String NULL_NAME_ERROR_MSG = "A contact's name cannot be null";
    private final String EMPTY_NAME_ERROR_MSG = "Contact's name should not be empty";
    private final String NULL_NOTES_ERROR_MSG = "Notes cannot be null";

    /**
     * Create a new Contact without notes
     *
     * @param id The contact's ID
     * @param name The contact's name
     * @throws IllegalArgumentException if the ID is 0 or negative,
     *                                  or if the name is an empty String
     * @throws NullPointerException if the name is null
     */
    public ContactImpl (int id, String name) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException(ID_ERROR_MSG);
        }
        if (name == null) {
            throw new NullPointerException(NULL_NAME_ERROR_MSG);
        }
        if (name.equals("")) {
            throw new IllegalArgumentException(EMPTY_NAME_ERROR_MSG);
        }
        this.initNotes();
        this.id = id;
        this.fullName = name;
    }

    /**
     * Create a new Contact with some notes
     *
     * @param notes Some notes about this contact
     *
     * @see ContactImpl#ContactImpl(int, String)
     */
    public ContactImpl (int id, String name, String notes) {
        this(id, name);
        try {
            this.addNotes(notes);
        } catch(NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Check if the ID argument is greater than 0
     *
     * @param id the ID to check
     * @return true if the ID is valid (gt 0), false if it's 0 or negative
     */
    private boolean isValidId (int id) {
        return id > 0;
    }

    /**
     * Initialises our list of Notes.
     * Avoid duplicate code in each constructor
     */
    private void initNotes () {
        if (this.notes == null) { this.notes = new ArrayList<>(); }
    }

    /**
     * @see Contact#getId()
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * @see Contact#getName()
     */
    @Override
    public String getName() {
        return this.fullName;
    }

    /**
     * @see Contact#getNotes()
     */
    @Override
    public String getNotes() {
        String result = "";
        for(String n : this.notes) {
            result += n;
        }
        return result;
    }

    /**
     * @see Contact#addNotes(String)
     * @throws NullPointerException if the argument is null
     */
    @Override
    public void addNotes(String note) {
        if (note == null) {
            throw new NullPointerException(NULL_NOTES_ERROR_MSG);
        }
        if (!note.equals("")) {
            this.notes.add(note);
        }
    }
}
