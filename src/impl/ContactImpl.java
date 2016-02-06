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

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getNotes() {
        return null;
    }

    @Override
    public void addNotes(String note) {

    }
}
