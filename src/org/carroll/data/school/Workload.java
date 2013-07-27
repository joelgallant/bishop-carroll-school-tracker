package org.carroll.data.school;

import java.util.Arrays;
import org.carroll.utils.Stats;
import org.joda.time.DateTime;

/**
 * Class used to store all workloads for each month in virtual "cache".
 *
 * @author Joel Gallant
 */
public class Workload {

    // Begins at september
    final static int[] workload,
            units;

    static {
        workload = new int[10];
        units = new int[10];
        Arrays.fill(workload, 50);
    }

    /**
     * Returns the workload value of that month. <br><b>Warning : Month index is
     * sort from September thru June. Be wary of this fact.</b>
     *
     * @param monthIndex index of the month (in index format described)
     * @return the workload of that month
     */
    public static int getWorkload(int monthIndex) {
        if (monthIndex > 9) {
            return 0;
        }
        return workload[monthIndex];
    }

    /**
     * Returns the units to do that month. <br><b>Warning : Month index is sort
     * from September thru June. Be wary of this fact.</b>
     *
     * @param monthIndex index of the month (in index format described)
     * @return the units to do that month
     */
    public static int getUnits(int monthIndex) {
        if (monthIndex > 9) {
            return 0;
        }
        reloadUnits();
        return units[monthIndex];
    }

    /**
     * Sets the workload of that month. <br><b>Warning : Month index is sort
     * from September thru June. Be wary of this fact.</b>
     *
     * @param monthIndex index of the month (in index format described)
     * @param workload the new value for the workload that month
     */
    public static void setWorkload(int monthIndex, int workload) {
        Workload.workload[monthIndex] = workload;
    }

    /**
     * Converts the month from normal index (1=January, etc.) to month index
     * specific to workload. (0 = September, etc.)
     *
     * @param trueMonth real life representation of the month
     * @return month index for workload
     */
    public static int convertMonth(int trueMonth) {
        int month;
        if (trueMonth > 8) {
            month = trueMonth - 9;
        } else if (trueMonth < 7) {
            month = trueMonth + 3;
        } else {
            month = 0;
        }
        return month;
    }

    static void reloadUnits() {
        int currentMonth = convertMonth(DateTime.now().getMonthOfYear());
        int unitsGoal = Stats.getTotalUnits();
        if (currentMonth > 9) {
            currentMonth = 0;
        }
        for (int x = 0; x < currentMonth; x++) {
            units[x] = Stats.getUnitsDoneInMonth(x);
        }
        for (int x = currentMonth; x < workload.length; x++) {
            double percentage = ((double) workload[x]) / ((double) totalWorkload(currentMonth));
            units[x] = (int) (percentage * unitsGoal);
        }
        if (totalUnits(currentMonth) != unitsGoal) {
            int remainingUnits = unitsGoal - totalUnits(currentMonth);
            if (remainingUnits > 0) {
                int index = currentMonth;
                for (int x = 0; x < remainingUnits; x++) {
                    index++;
                    if (index > units.length - 1) {
                        index = 0;
                    }
                    units[index]++;
                }
            } else {
                int index = currentMonth;
                for (int x = 0; x > remainingUnits; x++) {
                    index++;
                    if (index > units.length - 1) {
                        index = 0;
                    }
                    units[index]--;
                }
            }
        }
        for (int x = 0; x < units.length; x++) {
            units[x] -= Stats.getUnitsDoneInMonth(x);
            if (units[x] < 0) {
                units[x] = 0;
            }
        }
    }

    static int totalWorkload(int currentMonth) {
        int sum = 0;
        for (int x = currentMonth; x < workload.length; x++) {
            sum += workload[x];
        }
        return sum;
    }

    static int totalUnits(int currentMonth) {
        int sum = 0;
        for (int x = currentMonth; x < units.length; x++) {
            sum += units[x];
        }
        return sum;
    }
}
