package org.carroll.school;

import java.util.ArrayList;
import org.carroll.data.school.AllWork;
import org.carroll.data.settings.Settings;

/**
 * Interface for courses. Used to keep information about a course and it's
 * details.
 *
 * @author Joel Gallant
 */
public abstract class Course implements Comparable<Course> {

    String name = name();
    int credits = credits(),
            units = (credits == 5) ? 30
            : ((credits == 3) ? 15
            : ((credits == 1) ? 5
            : (credits * 5))),
            semester = semester();

    /**
     * Returns the name of the course.
     *
     * @return name of the course
     */
    protected abstract String name();

    /**
     * Returns the credits of the course.
     *
     * @return credits of the course
     */
    protected abstract int credits();

    /**
     * Returns the original value of the semester the user is doing the course
     * in.
     *
     * @return semester
     */
    protected abstract int semester();

    @Override
    public int compareTo(Course o) {
        return -(new Integer(o.getSemester()).compareTo(this.getSemester()) == 0 ? o.getName().compareTo(
                this.getName()) : new Integer(o.getSemester()).compareTo(this.getSemester()));
    }

    /**
     * Returns the string representation of the course.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Returns the name of the course
     *
     * @return name of the course
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the credits of the course
     *
     * @return credits of the course
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Returns the units the course has
     *
     * @return units the course has
     */
    public int getUnits() {
        return units;
    }

    /**
     * Returns how many units are complete in the course based on
     * {@link AllWork}.
     *
     * @return completed units
     */
    public int getCompletedUnits() {
        int completedUnits = 0;
        ArrayList<Work> work = AllWork.getInstance().getElements();
        for (Work workx : work) {
            if (workx.getCourseName().equalsIgnoreCase(getName())) {
                if (workx.isComplete()) {
                    completedUnits += workx.getUnits();
                }
            }
        }
        return completedUnits;
    }

    /**
     * Returns the semester that the user is doing the course in.
     *
     * @return when user is doing course
     */
    public int getSemester() {
        return semester;
    }

    /**
     * Returns the next incomplete piece of work to do in the course.
     *
     * @return next work
     * @exception NoSuchFieldException thrown when no next work exists
     */
    public Work getNextWork() throws NoSuchFieldException {
        int nextWorkIndex = 99999;
        int lowestOrder = 99999;
        for (int x = 0; x < AllWork.getInstance().getElements().size(); x++) {
            if (AllWork.getInstance().getElements().get(x).getCourseName().equals(getName())) {
                if (!AllWork.getInstance().getElements().get(x).isComplete()) {
                    if (AllWork.getInstance().getElements().get(x).getOrder() < lowestOrder) {
                        nextWorkIndex = x;
                        lowestOrder = AllWork.getInstance().getElements().get(x).getOrder();
                    }
                }
            }
        }
        if (nextWorkIndex != 99999) {
            return AllWork.getInstance().getElements().get(nextWorkIndex);
        } else {
            throw new NoSuchFieldException("No work left");
        }
    }

    /**
     * Returns a piece of work from the course that has that order value.
     *
     * @param order order value of work
     * @return work with order value
     * @throws NoSuchFieldException thrown when no work exists with that order
     * values
     */
    public Work getWork(int order) throws NoSuchFieldException {
        for (int x = 0; x < AllWork.getInstance().getElements().size(); x++) {
            if (AllWork.getInstance().getElements().get(x).getCourseName().equals(getName())) {
                if (AllWork.getInstance().getElements().get(x).getOrder() == order) {
                    return AllWork.getInstance().getElements().get(x);
                }
            }
        }
        throw new NoSuchFieldException("No work in slot " + order);
    }

    /**
     * Sets the name of the course
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the credits the course has
     *
     * @param credits new amount of credits
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Sets the units of the course
     *
     * @param units new units
     */
    public void setUnits(int units) {
        this.units = units;
    }

    /**
     * Sets the semester of the course
     *
     * @param semester new semester
     */
    public void setSemester(int semester) {
        try {
            if (semester > Integer.parseInt(Settings.getInstance().get("Semesters").getValue())) {
                this.semester = Integer.parseInt(Settings.getInstance().get("Semesters").getValue());
            } else if (semester < 0) {
                this.semester = 1;
            } else {
                this.semester = semester;
            }
        } catch (NoSuchFieldException | NumberFormatException ex) {
        }
    }
}
