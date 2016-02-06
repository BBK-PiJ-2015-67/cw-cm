package impl;

import spec.Contact;

/**
 * Implementation of Contact interface
 *
 * @see spec.Contact;
 * @author lmignot
 */
public class ContactImpl implements Contact {

    public ContactImpl (int id, String name) {

    }

    public ContactImpl (int id, String name, String notes) {

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
