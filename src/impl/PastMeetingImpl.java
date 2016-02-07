package impl;

import spec.PastMeeting;

/**
 * A Past Meeting is a meeting that has happened in the past
 * It contains notes.
 *
 * @see spec.PastMeeting
 * @see spec.Meeting
 *
 * @author lmignot
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

    /**
     * @see PastMeeting#getNotes()
     */
    @Override
    public String getNotes() {
        return null;
    }
}
