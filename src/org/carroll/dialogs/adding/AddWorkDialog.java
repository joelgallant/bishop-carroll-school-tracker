package org.carroll.dialogs.adding;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.carroll.data.school.AllCourses;
import org.carroll.data.school.AllWork;
import org.carroll.internal.InternalPanel;
import org.carroll.school.Work;
import org.carroll.utils.Logging;
import org.joda.time.IllegalFieldValueException;
import org.swing.Dialog;
import org.swing.Listeners;
import org.swing.dialogs.messages.ErrorMessage;

/**
 * Dialog that allows the user to add a new work.
 *
 * @author Joel Gallant
 */
public class AddWorkDialog extends Dialog {

    JButton done, cancel;
    KeyListener doneListener = new Listeners.DoneListener() {
        @Override
        public void enterPressed() {
            done.doClick();
        }
    };

    /**
     * Creates new work dialog.
     */
    public AddWorkDialog() {
        super("Add work");
    }

    void addWork(final String courseName, final String name, final int units, final int order, final int weight) {
        Logging.log("Adding new work - " + name);
        AllWork.getInstance().add(new Work(courseName) {
            @Override
            protected String name() {
                return name;
            }

            @Override
            protected int units() {
                return units;
            }

            @Override
            protected int order() {
                return order;
            }

            @Override
            protected int weight() {
                return weight;
            }
        });
        InternalPanel.WORK.refresh();
    }

    @Override
    protected void init() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        JComboBox<String> courseSelectorinit;
        if (AllCourses.getInstance().getNames().length > 0) {
            courseSelectorinit = new JComboBox<>(AllCourses.getInstance().getNames());
        } else {
            String[] defaultItem = {"No course"};
            courseSelectorinit = new JComboBox<>(defaultItem);
        }
        final JComboBox<String> courseSelector = courseSelectorinit;
        JLabel name = new JLabel("Work Name"),
                units = new JLabel("Units");
        final JTextField nameField = new JTextField(),
                unitsField = new JTextField(),
                orderField = new JTextField(),
                weightField = new JTextField();
        JLabel orderLabel = new JLabel("Order");
        JLabel weightLabel = new JLabel("Weight");
        done = new JButton("Add");
        cancel = new JButton("Cancel");

        name.setHorizontalAlignment(SwingConstants.CENTER);
        units.setHorizontalAlignment(SwingConstants.CENTER);
        orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        weightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        courseSelector.addKeyListener(doneListener);
        nameField.addKeyListener(doneListener);
        unitsField.addKeyListener(doneListener);
        orderField.addKeyListener(doneListener);
        weightField.addKeyListener(doneListener);
        done.addKeyListener(doneListener);
        cancel.addKeyListener(doneListener);

        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AllWork.getInstance().get(nameField.getText(), courseSelector.getSelectedItem().toString());
                    new ErrorMessage(new IllegalFieldValueException("Name", nameField.getText()), "Name already exists").createAndViewGUI();
                } catch (NoSuchFieldException ex) {
                    try {
                        addWork(courseSelector.getSelectedItem().toString(), nameField.getText(),
                                Integer.parseInt(unitsField.getText()), Integer.parseInt(orderField.getText()), Integer.parseInt(weightField.getText()));
                        dispose();
                    } catch (NumberFormatException ex1) {
                        new ErrorMessage(ex1, "Please input real numbers").createAndViewGUI();
                    }
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
        constraints.gridy = 4;
        constraints.gridx = 0;
        layout.addLayoutComponent(weightLabel, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(weightField, constraints);
        constraints.gridy = 5;
        constraints.gridx = 0;
        layout.addLayoutComponent(done, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(cancel, constraints);

        add(courseSelector);
        add(name);
        add(nameField);
        add(units);
        add(unitsField);
        add(orderLabel);
        add(orderField);
        add(weightLabel);
        add(weightField);
        add(done);
        add(cancel);
    }
}
