package org.carroll.dialogs.editing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.carroll.data.school.AllCourses;
import org.carroll.data.school.AllWork;
import org.carroll.data.school.Workload;
import org.carroll.internal.InternalPanel;
import org.carroll.school.Work;
import org.carroll.utils.Logging;
import org.joda.time.DateTime;
import org.swing.Dialog;
import org.swing.Listeners;
import org.swing.dialogs.messages.ErrorMessage;

/**
 * Dialog for editing work. Is custom to editing work.
 *
 * @author Joel Gallant
 */
public class EditWorkDialog extends Dialog {

    JButton done, cancel;
    KeyListener doneListener = new Listeners.DoneListener() {
        @Override
        public void enterPressed() {
            done.doClick();
        }
    };
    static Work currentWork;

    /**
     * Create dialog that selects a work by default.
     *
     * @param startingSelection name of starting work
     */
    public EditWorkDialog(String startingSelection, String courseSelection) {
        try {
            currentWork = AllWork.getInstance().get(startingSelection, courseSelection);
        } catch (NoSuchFieldException ex) {
            new ErrorMessage(ex, startingSelection+" was not found").createAndViewGUI();
        }
    }

    void editWork(String course, String name, int units, int order, boolean finished, int month, int weight) {
        Logging.log(name + " edited.");
        currentWork.setCourseName(course);
        currentWork.setName(name);
        currentWork.setUnits(units);
        currentWork.setOrder(order);
        currentWork.setComplete(finished, month);
        currentWork.setWeight(weight);
        InternalPanel.WORK.refresh();
    }

    @Override
    protected void init() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        setPreferredSize(new Dimension(275, 275));

        String[] months = {"September", "October", "November", "December", "January", "February", "March", "April", "May", "June"};

        JComboBox<String> courseSelectorinit;
        if (AllCourses.getInstance().getNames().length == 0) {
            String[] defaultOption = {"No Course"};
            courseSelectorinit = new JComboBox<>(defaultOption);
        } else {
            courseSelectorinit = new JComboBox<>(AllCourses.getInstance().getNames());
        }
        final JComboBox<String> courseSelector = courseSelectorinit;
        JLabel name = new JLabel("Name"),
                units = new JLabel("Units");
        final JTextField nameField = new JTextField(),
                unitsField = new JTextField();
        final JTextField orderField = new JTextField();
        JLabel orderLabel = new JLabel("Order");
        final JTextField weightField = new JTextField();
        JLabel weightLabel = new JLabel("Weight");
        final JCheckBox finished = new JCheckBox("Finished");
        JLabel monthLabel = new JLabel("Month completed");
        final JComboBox<String> month = new JComboBox<>(months);
        done = new JButton("Done");
        cancel = new JButton("Cancel");

        name.setHorizontalAlignment(SwingConstants.CENTER);
        units.setHorizontalAlignment(SwingConstants.CENTER);
        orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        weightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);

        courseSelector.setSelectedItem(currentWork.getCourseName());
        nameField.setText(currentWork.getName());
        nameField.setCaretPosition(0);
        unitsField.setText(String.valueOf(currentWork.getUnits()));
        orderField.setText(String.valueOf(currentWork.getOrder()));
        weightField.setText(String.valueOf(currentWork.getWeight()));
        finished.setSelected(currentWork.isComplete());
        if (currentWork.isComplete()) {
            month.setSelectedIndex(currentWork.getMonthComplete());
        } else {
            month.setSelectedIndex(Workload.convertMonth(DateTime.now().getMonthOfYear()));
        }

        courseSelector.addKeyListener(doneListener);
        nameField.addKeyListener(doneListener);
        unitsField.addKeyListener(doneListener);
        orderField.addKeyListener(doneListener);
        weightField.addKeyListener(doneListener);
        finished.addKeyListener(doneListener);
        month.addKeyListener(doneListener);
        done.addKeyListener(doneListener);
        cancel.addKeyListener(doneListener);

        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editWork(courseSelector.getSelectedItem().toString(), nameField.getText(), Integer.parseInt(unitsField.getText()), Integer.parseInt(orderField.getText()), finished.isSelected(), month.getSelectedIndex(), Integer.parseInt(weightField.getText()));
                    dispose();
                } catch (NumberFormatException ex) {
                    new ErrorMessage(ex, "Please input real numbers").createAndViewGUI();
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 8, 5, 8);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 2;
        layout.addLayoutComponent(courseSelector, constraints);
        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        layout.addLayoutComponent(name, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(nameField, constraints);
        constraints.gridy = 2;
        constraints.gridx = 0;
        layout.addLayoutComponent(units, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(unitsField, constraints);
        constraints.gridy = 3;
        constraints.gridx = 0;
        layout.addLayoutComponent(orderLabel, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(orderField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        layout.addLayoutComponent(weightLabel, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(weightField, constraints);
        constraints.gridwidth = 2;
        constraints.gridy = 5;
        constraints.gridx = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        layout.addLayoutComponent(monthLabel, constraints);
        constraints.gridy = 6;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(5, 8, 5, 8);
        layout.addLayoutComponent(finished, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(month, constraints);
        constraints.gridy = 7;
        constraints.gridx = 0;
        layout.addLayoutComponent(done, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(cancel, constraints);


        add(courseSelector);
        add(name);
        add(nameField);
        add(units);
        add(unitsField);
        add(orderField);
        add(orderLabel);
        add(weightLabel);
        add(weightField);
        add(monthLabel);
        add(finished);
        add(month);
        add(done);
        add(cancel);
    }

    @Override
    public void createAndViewGUI() {
        if(currentWork != null) {
            super.createAndViewGUI();
        }
    }
}
