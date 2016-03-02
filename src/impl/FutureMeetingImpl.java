package impl;

import spec.Contact;
import spec.FutureMeeting;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * Future Meeting
 * This class is simply used for type checking and/or down casting
 *
 * @see spec.FutureMeeting
 * @see spec.Meeting
 *
 * @author lmignot
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting, Serializable {

    /**
     * @see MeetingImpl
     *
     * @throws NullPointerException if the date or contacts are null
     * @throws IllegalArgumentException if the id is negative or 0,
     *                                  or if the set of contacts is empty
     */
    public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        super(id, date, contacts);
    }
}
