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
        if (id <= 0) {
            throw new IllegalArgumentException("IDs must be greater than 0");
        }
        if (name == null) {
            throw new NullPointerException("A contact's name cannot be null");
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("A contact's name should not be empty");
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
            throw new NullPointerException("Notes cannot be null");
        }
        if (!note.equals("")) {
            this.notes.add(note);
        }
    }
}
