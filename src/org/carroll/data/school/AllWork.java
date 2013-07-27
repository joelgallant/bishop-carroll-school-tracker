package org.carroll.data.school;

import java.util.ArrayList;
import java.util.Collections;
import org.carroll.data.ListOf;
import org.carroll.school.Work;

/**
 * Class that contains all the work the user needs. All work is stored here from
 * the file and used elsewhere through this class, acting as a virtual "cache".
 *
 * @author Joel Gallant
 */
public class AllWork extends ListOf<Work> {

    private static AllWork instance;

    AllWork() {
    }

    /**
     * Returns a singleton instance of {@link AllWork}.
     *
     * @return singleton instance
     */
    public static synchronized AllWork getInstance() {
        if (instance == null) {
            instance = new AllWork();
        }
        return instance;
    }

    @Override
    public void remove(String name) {
        for (int x = 0; x < getElements().size(); x++) {
            if (getElements().get(x).getName().equals(name)) {
                getElements().remove(x);
                return;
            }
        }
    }

    public void remove(String name, String course) {
        for (int x = 0; x < getElements().size(); x++) {
            if (getElements().get(x).getName().equals(name)) {
                if (getElements().get(x).getCourseName().equals(course)) {
                    remove(getElements().get(x));
                    return;
                }
            }
        }
    }

    /**
     * Removes all work in a specific course.
     *
     * @param course course name
     */
    public void removeAllFrom(String course) {
        ArrayList<Work> toRemove = new ArrayList<>();
        for (int x = 0; x < getElements().size(); x++) {
            if (getElements().get(x).getCourseName().equals(course)) {
                toRemove.add(getElements().get(x));
            }
        }
        for (int x = 0; x < toRemove.size(); x++) {
            remove(toRemove.get(x));
        }
    }

    @Override
    public Work get(String name) throws NoSuchFieldException {
        for (int x = 0; x < getElements().size(); x++) {
            if (getElements().get(x).getName().equals(name)) {
                return getElements().get(x);
            }
        }
        throw new NoSuchFieldException("No work named " + name + " found.");
    }

    public Work get(String name, String course) throws NoSuchFieldException {
        for (int x = 0; x < getElements().size(); x++) {
            if (getElements().get(x).getName().equals(name)) {
                if(getElements().get(x).getCourseName().equals(course)) {
                    return getElements().get(x);
                }
            }
        }
        throw new NoSuchFieldException("No work named " + name + " found.");
    }

    /**
     * Returns an array of the names of all current work.
     *
     * @return array of work names
     */
    public String[] getNames() {
        String[] names = new String[getElements().size()];
        for (int x = 0; x < getElements().size(); x++) {
            names[x] = getElements().get(x).getName();
        }
        return names;
    }

    @Override
    public void sort() {
        Collections.sort(getElements());
    }
}