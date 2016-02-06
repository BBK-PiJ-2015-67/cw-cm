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
    private List<String> notes;

    public ContactImpl (int id, String name) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("IDs must be greater than 0");
        }
        this.initNotes();
        this.id = id;
        this.fullName = name;
    }

    public ContactImpl (int id, String name, String notes) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("IDs must be greater than 0");
        }
        this.initNotes();

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
        this.notes = new ArrayList<>();
    }

    /**
     * @see Contact#getId()
     */
    @Override
    public int getId() {
        return 0;
    }

    /**
     * @see Contact#getName()
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * @see Contact#getNotes()
     */
    @Override
    public String getNotes() {
        return null;
    }

    /**
     * @see Contact#addNotes(String)
     */
    @Override
    public void addNotes(String note) {

    }
}
