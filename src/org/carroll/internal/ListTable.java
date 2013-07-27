package org.carroll.internal;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Comparator;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.swing.Refreshable;

/**
 * Model for classes that use custom table models to store data.
 *
 * @author Joel Gallant
 */
public abstract class ListTable extends InternalPanel implements Refreshable {

    /**
     * Main table;
     */
    protected JTable main,
            /**
             * Table at the bottom that displays totals.
             */
            totals;

    /**
     * Creates new list table. Requires the instances of the table models.
     *
     * @param m Main table model.
     * @param t Totals table model.
     */
    public ListTable(JTable m, JTable t) {
        this.main = m;
        this.totals = t;

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        JScrollPane pane = new JScrollPane(main);

        main.setAutoCreateRowSorter(false);

        totals.setFont(totals.getFont().deriveFont(Font.BOLD));
        totals.setEnabled(false);

        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.weightx = 1;
        layout.addLayoutComponent(pane, constraints);
        constraints.weighty = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 2, 0, 17);
        layout.addLayoutComponent(totals, constraints);

        add(pane);
        add(totals);
    }
    
    protected abstract void sortTable();

    /**
     * Comparator for tables to sort by integer.
     */
    public static class IntComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * Comparator for tables to sort by string (Alphabetically).
     */
    public static class StringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    @Override
    public void refresh() {
        try {
            sortTable();
            ((AbstractTableModel) main.getModel()).fireTableDataChanged();
            ((AbstractTableModel) totals.getModel()).fireTableDataChanged();
        } catch (Exception ex) {
        }
    }
}
