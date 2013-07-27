package org.carroll.internal.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import org.carroll.data.school.AllCourses;
import org.carroll.internal.InternalPanel;
import org.carroll.school.Course;
import org.carroll.school.Work;
import org.carroll.utils.Stats;

/**
 * To do panel - displays data about what the user should do in the upcoming
 * week / month.
 *
 * @author Joel Gallant
 */
public class ToDoPanel extends InternalPanel {

    static ArrayList<Work> toDoWeek = new ArrayList<>(),
            toDoMonth = new ArrayList<>();
    JLabel[] stats = new JLabel[12];
    WorkTable week = new WorkTable(toDoWeek),
            month = new WorkTable(toDoMonth);

    /**
     * Creates the to do panel.
     */
    public ToDoPanel() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagLayout tablePanelLayout = new GridBagLayout();
        JPanel statsPanel = new JPanel(new GridLayout(13, 1)),
                tablePanel = new JPanel(tablePanelLayout);
        JLabel statsTitle = new JLabel("Stats"),
                weekTitle = new JLabel("Work for this week"),
                monthTitle = new JLabel("Work for this month");
        JTable weekTable = new JTable(week),
                monthTable = new JTable(month);
        JScrollPane weekPane = new JScrollPane(weekTable),
                monthPane = new JScrollPane(monthTable);

        weekTitle.setFont(weekTitle.getFont().deriveFont(Font.BOLD, (float) 20));
        weekTitle.setHorizontalAlignment(SwingConstants.CENTER);
        monthTitle.setFont(weekTitle.getFont());
        monthTitle.setHorizontalAlignment(SwingConstants.CENTER);
        statsTitle.setFont(weekTitle.getFont());
        statsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        weekTable.setAutoCreateRowSorter(true);
        monthTable.setAutoCreateRowSorter(true);

        statsPanel.add(statsTitle);
        for (int x = 0; x < stats.length; x++) {
            stats[x] = new JLabel();
            stats[x].setFont(new Font("Arial", Font.PLAIN, 14));
            stats[x].setHorizontalAlignment(SwingConstants.CENTER);
            statsPanel.add(stats[x]);
        }

        GridBagConstraints tableConstraints = new GridBagConstraints();
        tableConstraints.fill = GridBagConstraints.BOTH;
        tableConstraints.weightx = 1;
        tableConstraints.gridx = 0;
        tablePanelLayout.addLayoutComponent(weekTitle, tableConstraints);
        tableConstraints.gridy = 2;
        tablePanelLayout.addLayoutComponent(monthTitle, tableConstraints);
        tableConstraints.gridy = 1;
        tableConstraints.weighty = 1;
        tablePanelLayout.addLayoutComponent(weekPane, tableConstraints);
        tableConstraints.gridy = 3;
        tablePanelLayout.addLayoutComponent(monthPane, tableConstraints);

        tablePanel.add(weekTitle);
        tablePanel.add(weekPane);
        tablePanel.add(monthTitle);
        tablePanel.add(monthPane);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        layout.addLayoutComponent(tablePanel, constraints);
        constraints.gridx = 1;
        constraints.weightx = 0;
        layout.addLayoutComponent(statsPanel, constraints);

        add(tablePanel);
        add(statsPanel);
    }

    @Override
    public void refresh() {
        updateStats();
        updateTable(toDoWeek, week, Stats.getRemainingUnitsThisWeek(), null);
        updateTable(toDoMonth, month, Stats.getRemainingUnitsThisMonth(), toDoWeek);
    }

    void updateStats() {
        stats[0].setText(Stats.getCompletePercentage() + "% of work complete");
        stats[1].setText("Units done this month : " + Stats.getUnitsCompleteThisMonth());
        stats[2].setText("Remaining units this year : " + Stats.getRemainingUnits());
        stats[3].setText("Remaining units this week : " + Stats.getRemainingUnitsThisWeek());
        stats[4].setText("Units to do this month : " + Stats.getRemainingUnitsThisMonth());
        stats[5].setText("Average units per day : " + Stats.getAverageUnitsPerDay());
        stats[6].setText("School days remaining : " + Stats.getRemainingDays());
        stats[7].setText("School weeks remaining : " + Stats.getRemainingWeeks());
        stats[8].setText("School months remaining : " + Stats.getRemainingMonths());
        stats[9].setText("Current semester : " + Stats.getCurrentSemester());
        stats[10].setText("Courses : " + Stats.getCourses());
        stats[11].setText("Courses this semester : " + Stats.getCoursesThisSemester());
    }

    void updateTable(ArrayList<Work> work, AbstractTableModel model, int goalUnits, ArrayList<Work> original) {
        //DETERMINE WHICH WORK TO INCLUDE
        int totalUnits = 0;
        work.clear();
        if (original != null) {
            for (Work x : original) {
                work.add(x);
                totalUnits += x.getUnits();
            }
        }
        recurse(AllCourses.getInstance().getElements(), work, totalUnits, goalUnits);
        model.fireTableDataChanged();
    }

    void recurse(ArrayList<Course> courses, ArrayList<Work> goingTo, int totalUnits, int goalUnits) {
        if (goalUnits == 0) {
            return;
        }
        ArrayList<Work> work = new ArrayList<>();
        for (Course course : courses) {
            if (course.getSemester() == Stats.getCurrentSemester()) {
                try {
                    work.add(course.getNextWork());
                } catch (NoSuchFieldException ex) {
                }
            }
        }
        for (Work w : work) {
            if (!goingTo.contains(w)) {
                if (totalUnits + w.getUnits() <= goalUnits) {
                    goingTo.add(w);
                    totalUnits += w.getUnits();
                }
            }
            if (totalUnits >= goalUnits) {
                return;
            }
        }
        int overNext = 1;
        ArrayList<Integer> coursesOver = new ArrayList<>();
        for (int x = 0; totalUnits < goalUnits; x++) {
            if (x == courses.size()) {
                x = 0;
                overNext++;
            }
            try {
                Work w = courses.get(x).getWork(courses.get(x).getNextWork().getOrder() + overNext);
                if (!goingTo.contains(w)) {
                    if (!w.isComplete()) {
                        if (AllCourses.getInstance().get(w.getCourseName()).getSemester() == Stats.getCurrentSemester()) {
                            goingTo.add(w);
                            totalUnits += w.getUnits();
                        }
                    }
                }
            } catch (NoSuchFieldException ex) {
                if (!coursesOver.contains(x)) {
                    coursesOver.add(x);
                }
            }
            if (coursesOver.size() >= courses.size()) {
                break;
            }
        }
    }

    static class WorkTable extends AbstractTableModel {

        ArrayList<Work> work;
        String[] labels = {"Work Name", "Units", "Course", "Weight"};

        public WorkTable(ArrayList<Work> work) {
            this.work = work;
        }

        @Override
        public String getColumnName(int column) {
            return labels[column];
        }

        @Override
        public int getRowCount() {
            return work.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case (0):
                    return work.get(rowIndex).getName();
                case (1):
                    return work.get(rowIndex).getUnits();
                case (2):
                    return work.get(rowIndex).getCourseName();
                case (3):
                    return work.get(rowIndex).getWeight();
                default:
                    return "";
            }
        }
    }
}
