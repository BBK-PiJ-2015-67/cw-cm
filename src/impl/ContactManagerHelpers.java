package impl;

import spec.Meeting;

import java.util.List;
import java.util.Objects;

/**
 * Helper methods for ContactManager
 * @author lmignot
 */
final class ContactManagerHelpers {

    private ContactManagerHelpers () {}

    /**
     * Check for null values in an array of objects
     *
     * @param args The arguments to check
     * @throws NullPointerException if any of the arguments are null
     */
    @SafeVarargs
    static <T> void requireNonNullArguments(T... args) {
        for (T t : args) {
            Objects.requireNonNull(t);
        }
    }

    /**
     * Removes any duplicates from a list of meetings<br>
     * A meeting B is duplicate of A ONLY if<br>
     *     <ul>
     *         <li>B contains ALL contacts in A</li>
     *         <li>B occurs at the same TIME as A</li>
     *     </ul>
     *
     * @param meetings The list of meetings to remove duplicates from
     * @return The list of meetings, with duplicates removed
     */
    static List<Meeting> getDistinctMeetings(List<Meeting> meetings) {
        for (int i = 0; i < meetings.size(); i++) {
            Meeting orig = meetings.get(i);
            for (int k = i + 1; k < meetings.size(); k++) {
                Meeting test = meetings.get(k);
                if (orig.getId() != test.getId()
                        && orig.getContacts().containsAll(test.getContacts())
                        && test.getContacts().containsAll(orig.getContacts())
                        && orig.getDate().compareTo(test.getDate()) == 0) {
                    meetings.remove(k);
                }
            }
        }
        return meetings;
    }
}
