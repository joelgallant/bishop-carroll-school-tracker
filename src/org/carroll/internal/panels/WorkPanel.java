package org.carroll.internal.panels;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.carroll.data.school.AllWork;
import org.carroll.dialogs.editing.EditWorkDialog;
import org.carroll.internal.ListTable;

/**
 * Work panel - displays data about what work the user needs to do.
 *
 * @author Joel Gallant
 */
public class WorkPanel extends ListTable {

    static JTable workTable = new JTable(new WorkTable()),
            totalsTable = new JTable(new TotalsTable());

    /**
     * Creates the work panel.
     */
    public WorkPanel() {
        super(workTable, totalsTable);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(workTable.getModel());
        sorter.setComparator(0, new StringComparator());
        sorter.setComparator(1, new IntComparator());
        sorter.setComparator(2, new StringComparator());
        sorter.setComparator(3, new IntComparator());
        sorter.setComparator(4, new StringComparator());
        workTable.setRowSorter(sorter);
        workTable.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    new EditWorkDialog(target.getValueAt(row, 0).toString(), target.getValueAt(row, 2).toString()).createAndViewGUI();
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
     * Returns the name of the work that is selected on the table.
     *
     * @return name of selected work
     */
    public static String getSelectedWorkName() throws NoSuchFieldException {
        if (workTable.getRowCount() > 0) {
            try {
                int selected = workTable.getSelectedRows()[0];
                return workTable.getValueAt(selected, 0).toString();
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new NoSuchFieldException("No work selected");
            }
        } else {
            return null;
        }
    }

    public static String getSelectedWorkCourse() throws NoSuchFieldException {
        if (workTable.getRowCount() > 0) {
            try {
                int selected = workTable.getSelectedRows()[0];
                return workTable.getValueAt(selected, 2).toString();
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new NoSuchFieldException("No work selected");
            }
        } else {
            return null;
        }
    }

    @Override
    protected void sortTable() {
        AllWork.getInstance().sort();
    }

    static class WorkTable extends AbstractTableModel {

        String[] labels = {"Work Name", "Units", "Course", "Order","Weight", "Completed"};

        @Override
        public String getColumnName(int column) {
            return labels[column];
        }

        @Override
        public int getRowCount() {
            return AllWork.getInstance().getElements().size();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case (0):
                    return AllWork.getInstance().getElements().get(rowIndex).getName();
                case (1):
                    return AllWork.getInstance().getElements().get(rowIndex).getUnits();
                case (2):
                    return AllWork.getInstance().getElements().get(rowIndex).getCourseName();
                case (3):
                    return AllWork.getInstance().getElements().get(rowIndex).getOrder();
                case (4):
                    return AllWork.getInstance().getElements().get(rowIndex).getWeight();
                case (5):
                    return AllWork.getInstance().getElements().get(rowIndex).isComplete() ? "Complete" : "Not complete";
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
            return 6;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            int units = 0, complete = 0;
            for (int x = 0; x < AllWork.getInstance().getElements().size(); x++) {
                units += AllWork.getInstance().getElements().get(x).getUnits();
                if (AllWork.getInstance().getElements().get(x).isComplete()) {
                    complete++;
                }
            }
            switch (columnIndex) {
                case (0):
                    return "Total";
                case (1):
                    return units;
                case (5):
                    return complete;
                default:
                    return "";
            }
        }
    }
}
