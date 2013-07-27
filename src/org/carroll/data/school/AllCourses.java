package org.carroll.data.school;

import java.util.Collections;
import org.carroll.data.ListOf;
import org.carroll.school.Course;

/**
 * Class that contains all the courses the user needs. All courses are stored
 * here from the file and used elsewhere through this class, acting as a virtual
 * "cache".
 *
 * @author Joel Gallant
 */
public class AllCourses extends ListOf<Course> {

    private static AllCourses instance;

    AllCourses() {
    }

    /**
     * Returns a singleton instance of {@link AllCourses}.
     *
     * @return singleton instance
     */
    public static AllCourses getInstance() {
        if (instance == null) {
            instance = new AllCourses();
        }
        return instance;
    }

    @Override
    public void remove(String name) {
        for (int x = 0; x < getElements().size(); x++) {
            if (getElements().get(x).getName().equals(name)) {
                getElements().remove(x);
            }
        }
        AllWork.getInstance().removeAllFrom(name);
    }

    @Override
    public Course get(String name) throws NoSuchFieldException {
        for (int x = 0; x < getElements().size(); x++) {
            if (getElements().get(x).getName().equals(name)) {
                return getElements().get(x);
            }
        }
        throw new NoSuchFieldException("No course named " + name + " found.");
    }

    /**
     * Returns an array of the names of all current courses.
     *
     * @return array of course names
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
