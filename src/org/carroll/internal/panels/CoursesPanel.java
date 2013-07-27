package org.carroll.internal.panels;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.carroll.data.school.AllCourses;
import org.carroll.dialogs.editing.EditCourseDialog;
import org.carroll.internal.ListTable;
import org.carroll.school.Course;

/**
 * Courses panel - displays data about what courses the user is doing.
 *
 * @author Joel Gallant
 */
public class CoursesPanel extends ListTable {

    static JTable coursesTable = new JTable(new CoursesTable()),
            totalsTable = new JTable(new TotalsTable());

    /**
     * Creates the courses panel.
     */
    public CoursesPanel() {
        super(coursesTable, totalsTable);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(coursesTable.getModel());
        sorter.setComparator(0, new StringComparator());
        sorter.setComparator(1, new IntComparator());
        sorter.setComparator(2, new IntComparator());
        sorter.setComparator(3, new IntComparator());
        sorter.setComparator(4, new IntComparator());
        coursesTable.setRowSorter(sorter);
        coursesTable.addMouseListener(new MouseInputListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    new EditCourseDialog(target.getValueAt(row, 0).toString()).createAndViewGUI();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    /**
     * Returns the name of the course that is selected on the table.
     *
     * @return name of selected course
     */
    public static String getSelectedWork() {
        if(coursesTable.getRowCount() > 0) {
            int selected = coursesTable.getSelectedRows().length == 0 ? 0 : coursesTable.getSelectedRows()[0];
            return coursesTable.getValueAt(selected, 0).toString();
        }else {
            return null;
        }
    }

    @Override
    protected void sortTable() {
        AllCourses.getInstance().sort();
    }

    static class CoursesTable extends AbstractTableModel {

        String[] labels = {"Name", "Credits", "Units", "Completed Units", "Semester"};

        @Override
        public String getColumnName(int column) {
            return labels[column];
        }

        @Override
        public int getRowCount() {
            return AllCourses.getInstance().getElements().size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case (0):
                    return AllCourses.getInstance().getElements().get(rowIndex).getName();
                case (1):
                    return AllCourses.getInstance().getElements().get(rowIndex).getCredits();
                case (2):
                    return AllCourses.getInstance().getElements().get(rowIndex).getUnits();
                case (3):
                    return AllCourses.getInstance().getElements().get(rowIndex).getCompletedUnits();
                case (4):
                    return AllCourses.getInstance().getElements().get(rowIndex).getSemester();
                default:
                    return "";
            }
        }
    }

    static class TotalsTable extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return getTotal(columnIndex);
        }

        String getTotal(int index) {
            int credits = 0, units = 0, completed = 0;
            for (Course course : AllCourses.getInstance().getElements()) {
                credits += course.getCredits();
                units += course.getUnits();
                completed += course.getCompletedUnits();
            }
            switch (index) {
                case (0):
                    return "Total";
                case (1):
                    return String.valueOf(credits);
                case (2):
                    return String.valueOf(units);
                case (3):
                    return String.valueOf(completed);
                case (4):
                    return "";
            }
            return null;
        }
    }
}
