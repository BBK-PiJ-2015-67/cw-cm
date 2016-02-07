package impl;

import spec.Contact;
import spec.FutureMeeting;

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
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

    /**
     * @see impl.MeetingImpl
     */
    public FutureMeetingImpl (int id, Calendar date, Set<Contact> contacts) {
        super(id, date, contacts);
    }
}
