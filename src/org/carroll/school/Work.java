package org.carroll.school;

import org.carroll.data.school.AllCourses;

/**
 * Interface for work.
 *
 * @author Joel Gallant
 */
public abstract class Work implements Comparable<Work> {

    boolean complete = false;
    String courseName = "Unknown",
            name = name();
    int units = units(),
            order = order(),
            weight = weight();
    int monthComplete = -1;

    /**
     * Creates new work with the course name in the parameter.
     *
     * @param courseName name of works course
     */
    public Work(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Returns the name of the work.
     *
     * @return name of the work
     */
    protected abstract String name();

    /**
     * Returns the amount of units
     *
     * @return units of the work
     */
    protected abstract int units();

    /**
     * Returns the order in the course that the work is.
     *
     * @return order in course
     */
    protected abstract int order();

    /**
     * Returns the weight of the work in the course (How much it is worth)
     *
     * @return how much work is worth in course grade
     */
    protected abstract int weight();

    /**
     * Compares the work to different work. Used to sort.
     *
     * @param o other work
     * @return the difference between the two work
     */
    @Override
    public int compareTo(Work o) {
        int num = 0;
        try {
            num = -new Integer(AllCourses.getInstance().get(o.getCourseName()).getSemester()).compareTo(AllCourses.getInstance().get(this.getCourseName()).getSemester());
        } catch (NoSuchFieldException ex) {
        }
        if (num == 0) {
            num = -o.getCourseName().compareTo(this.getCourseName());
        }
        if (num == 0) {
            num = -new Integer(o.getOrder()).compareTo(this.getOrder());
        }
        return num;
    }

    /**
     * Tests to see if the two work are the same name.
     *
     * @param obj other work
     * @return whether the two work are the same
     */
    public boolean equals(Work obj) {
        return this.getName().equals(obj.getName());
    }

    /**
     * Returns the current course's name
     *
     * @return course's name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Returns the name of the work
     *
     * @return name of the work
     */
    public String getName() {
        return name;
    }

    /**
     * Returns how many units the work is worth
     *
     * @return units of the work
     */
    public int getUnits() {
        return units;
    }

    /**
     * Returns the priority of the work
     *
     * @return priority of work
     */
    public int getOrder() {
        return order;
    }

    /**
     * Returns the weight of the work in the course.
     *
     * @return returns how much work is worth
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Returns whether the work is complete
     *
     * @return whether the work is complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Returns the month that the work was completed in (In Workload month
     * format)
     *
     * @return month completed in
     */
    public int getMonthComplete() {
        return monthComplete;
    }

    /**
     * Sets the name of the course the work is part of
     *
     * @param courseName name of the course
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Sets the name of the work
     *
     * @param name new name of work
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the amount of units the work is worth
     *
     * @param units new unit count
     */
    public void setUnits(int units) {
        this.units = units;
    }

    /**
     * Sets the order of the work
     *
     * @param order order in which user should do work
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Sets the weight of the work.
     *
     * @param weight how much work is worth in the course
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Sets whether the work is complete or not
     *
     * @param finished new complete value
     * @param monthComplete month the work was completed in
     */
    public void setComplete(boolean finished, int monthComplete) {
        complete = finished;
        if (finished) {
            this.monthComplete = monthComplete;
        } else {
            this.monthComplete = -1;
        }
    }
}
