package org.carroll.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.table.TableModel;
import org.carroll.data.school.AllCourses;
import org.carroll.data.school.AllWork;
import org.carroll.data.school.Workload;
import org.carroll.data.settings.Settings;
import org.carroll.school.Course;
import org.carroll.school.Work;
import org.joda.time.DateTime;
import org.swing.dialogs.messages.ErrorMessage;

/**
 * Methods to retrieve stats from particular components of the program.
 *
 * @author Joel Gallant
 */
public class Stats {

    static TableModel schoolDates;
    static int remainingDays;

    static {
        try {
            schoolDates = Excel.getTableModelFrom(ClassLoader.getSystemResourceAsStream("org/carroll/resources/SchoolDates.xls"));
        } catch (Exception ex) {
            new ErrorMessage(ex, "There was an error getting system resource stream with school dates.").createAndViewGUI();
        }
    }

    /**
     * Returns the total amount of units to be done (based on courses - No
     * Course work is discounted).
     *
     * @return number of units
     */
    public static int getTotalUnits() {
        int units = 0;
        for (Course course : AllCourses.getInstance().getElements()) {
            units += course.getUnits();
        }
        return units;
    }

    /**
     * Returns the total amount of work to be done.
     *
     * @return amount of work
     */
    public static int getTotalWork() {
        return AllWork.getInstance().getElements().size();
    }

    /**
     * Returns the total amount of units that are done.
     *
     * @return number of units completed
     */
    public static int getTotalCompletedUnits() {
        int completed = 0;
        for (Course course : AllCourses.getInstance().getElements()) {
            completed += course.getCompletedUnits();
        }
        return completed;
    }

    /**
     * Returns the percentage of work finished out of 100.
     *
     * @return how much work is done
     */
    public static double getCompletePercentage() {
        return (double) Math.round((((double) getTotalCompletedUnits() / (double) getTotalUnits()) * 100) * 100) / 100;
    }

    /**
     * Returns the amount of units the user should complete in the month.
     *
     * @return units this month
     */
    public static int getUnitsCompleteThisMonth() {
        return getUnitsDoneInMonth(Workload.convertMonth(DateTime.now().getMonthOfYear()));
    }

    /**
     *
     * @param month
     * @return
     */
    public static int getUnitsDoneInMonth(int month) {
        int work = 0;
        for (Work x : AllWork.getInstance().getElements()) {
            if (x.getMonthComplete() == month) {
                work += x.getUnits();
            }
        }
        return work;
    }

    /**
     *
     * @param month
     * @param course
     * @return
     */
    public static int getUnitsDoneInMonth(int month, String course) {
        int work = 0;
        for (Work x : AllWork.getInstance().getElements()) {
            if (x.getMonthComplete() == month) {
                if (x.getCourseName().equals(course)) {
                    work += x.getUnits();
                }
            }
        }
        return work;
    }

    /**
     * Returns the average unit count per day for the remainder of the year.
     *
     * @return average units per day
     */
    public static double getAverageUnitsPerDay() {
        return (double) Math.round(((double) getRemainingUnits() / (double) getRemainingDays()) * 10) / 10;
    }

    /**
     * Returns the remaining units the user needs to complete.
     *
     * @return units left to do
     */
    public static int getRemainingUnits() {
        return getTotalUnits() - getTotalCompletedUnits();
    }

    /**
     * Returns the amount of units there are left to do this week based on
     * workload and day of the week.
     *
     * @return remaining units to be done this week
     */
    public static int getRemainingUnitsThisWeek() {
        int daysLeft = (new GregorianCalendar().getActualMaximum(Calendar.DAY_OF_MONTH) - DateTime.now().getDayOfMonth());
        double weeksLeft = (double) daysLeft / 7;
        int units;
        if (daysLeft < 7) {
            return getRemainingUnitsThisMonth();
        } else {
            units = (int) Math.round(getRemainingUnitsThisMonth() / weeksLeft);
        }
        if (DateTime.now().getDayOfWeek() < 6) {
            int daysLeftInWeek = 6 - DateTime.now().getDayOfWeek();
            units = (int) ((double) units / 5 * daysLeftInWeek);
        }
        return units;
    }

    /**
     * Returns the amount of school days remaining.
     *
     * @return remaining school days
     */
    public static int getRemainingDays() {
        if (remainingDays == 0) {
            if (DateTime.now().getDayOfYear() < 181) {
                // If date is past jan, only add jan to june
                for (int x = DateTime.now().getDayOfYear() - 1; x < 180; x++) {
                    if (schoolDates.getValueAt(x, 0).equals("y")) {
                        remainingDays++;
                    }
                }
            } else {
                // If date is before jan, add all jan to june and date to dec
                for (int x = 0; x < 180; x++) {
                    if (schoolDates.getValueAt(x, 0).equals("y")) {
                        remainingDays++;
                    }
                }
                // All date to dec
                for (int x = DateTime.now().getDayOfYear() - 1; x < 365; x++) {
                    if (schoolDates.getValueAt(x, 0).equals("y")) {
                        remainingDays++;
                    }
                }
            }
        }
        return remainingDays;
    }

    /**
     * Returns the amount of school weeks remaining based on the days.
     *
     * @return remaining school weeks
     */
    public static int getRemainingWeeks() {
        return getRemainingDays() / 5;
    }

    /**
     * Returns the amount of months remaining based on the day of the month.
     *
     * @return remaining months of school
     */
    public static int getRemainingMonths() {
        int restOfYear;
        if (DateTime.now().getMonthOfYear() > 8) {
            if (DateTime.now().getDayOfMonth() > 15) {
                restOfYear = (12 - DateTime.now().getMonthOfYear()) + 6;
            } else {
                restOfYear = (12 - DateTime.now().getMonthOfYear() + 1) + 6;
            }
        } else if (DateTime.now().getMonthOfYear() < 7) {
            if (DateTime.now().getDayOfMonth() > 15) {
                restOfYear = 6 - DateTime.now().getMonthOfYear();
            } else {
                restOfYear = 6 - DateTime.now().getMonthOfYear() + 1;
            }
        } else {
            restOfYear = 10;
        }
        return restOfYear;
    }

    /**
     * Returns the amount of units to be done this month based on the workload.
     *
     * @return units this month
     */
    public static int getRemainingUnitsThisMonth() {
        return Workload.getUnits(Workload.convertMonth(DateTime.now().getMonthOfYear()));
    }

    /**
     * Returns the current semester based on the amount of semesters (Only 4,2
     * and 1 are acceptable here.)
     *
     * @return the current semester
     */
    public static int getCurrentSemester() {
        int semesters;
        try {
            semesters = Integer.parseInt(Settings.getInstance().get("Semesters").getValue());
        } catch (NoSuchFieldException ex) {
            semesters = 2;
        }
        if (semesters == 2) {
            if (DateTime.now().getMonthOfYear() < 7 && DateTime.now().getMonthOfYear() > 1) {
                return 2;
            } else {
                return 1;
            }
        } else if (semesters == 4) {
            switch (DateTime.now().getMonthOfYear()) {
                case (9):
                case (10):
                    return 1;
                case (11):
                case (12):
                case (1):
                    return 2;
                case (2):
                case (3):
                    return 3;
                case (4):
                case (5):
                case (6):
                    return 4;
                default:
                    return 1;
            }
        } else {
            return 1;
        }
    }

    /**
     * Returns the amount of courses being done by the user.
     *
     * @return amount of courses
     */
    public static int getCourses() {
        return AllCourses.getInstance().getElements().size();
    }

    /**
     * Returns the amount of courses being done this semester.
     *
     * @return courses this semester
     */
    public static int getCoursesThisSemester() {
        int semester = getCurrentSemester();
        int courses = 0;
        for (Course c : AllCourses.getInstance().getElements()) {
            if (c.getSemester() == semester) {
                courses++;
            }
        }
        return courses;
    }
}
