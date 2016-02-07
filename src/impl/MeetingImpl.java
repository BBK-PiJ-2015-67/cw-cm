package impl;

import spec.Contact;
import spec.Meeting;

import java.util.Calendar;
import java.util.Set;

/**
 * Implementation of a Meeting
 * @see spec.Meeting
 *
 * @author lmignot
 */
public abstract class MeetingImpl implements Meeting {
    
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Calendar getDate() {
        return null;
    }

    @Override
    public Set<Contact> getContacts() {
        return null;
    }
}
