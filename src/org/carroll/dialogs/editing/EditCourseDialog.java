package org.carroll.dialogs.editing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.carroll.data.school.AllCourses;
import org.carroll.data.settings.Settings;
import org.carroll.internal.InternalPanel;
import org.carroll.school.Course;
import org.carroll.utils.Logging;
import org.swing.Dialog;
import org.swing.Listeners;
import org.swing.dialogs.messages.ErrorMessage;

/**
 * Dialog for editing a course. Is custom to editing courses.
 *
 * @author Joel Gallant
 */
public class EditCourseDialog extends Dialog {

    String startingSelection;
    JButton save, done, cancel;
    KeyListener doneListener = new Listeners.DoneListener() {
        @Override
        public void enterPressed() {
            done.doClick();
        }
    };
    static Course currentCourse;

    /**
     * Creates dialog with default course selected.
     */
    public EditCourseDialog() {
    }

    /**
     * Creates dialog that selects a course by default.
     *
     * @param startingSelection default selection
     */
    public EditCourseDialog(String startingSelection) {
        this.startingSelection = startingSelection;
    }

    void editCourse(String name, int credits, int units, int semester) {
        Logging.log(name + " edited.");
        currentCourse.setName(name);
        currentCourse.setCredits(credits);
        currentCourse.setUnits(units);
        currentCourse.setSemester(semester);
        InternalPanel.COURSES.refresh();
    }

    @Override
    protected void init() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        setPreferredSize(new Dimension(300, 250));

        Integer[] creditArray = {1, 3, 5};
        Integer[] semesterArray;
        try {
            semesterArray = new Integer[Integer.parseInt(Settings.getInstance().get("Semesters").getValue())];
            for (int x = 0; x < Integer.parseInt(Settings.getInstance().get("Semesters").getValue()); x++) {
                semesterArray[x] = x + 1;
            }
        } catch (NoSuchFieldException ex) {
            semesterArray = new Integer[2];
            semesterArray[0] = 1;
            semesterArray[1] = 2;
        }

        final JComboBox<String> course = new JComboBox<>(AllCourses.getInstance().getNames());
        JLabel nameLabel = new JLabel("Name"),
                creditsLabel = new JLabel("Credits"),
                unitsLabel = new JLabel("Units"),
                semesterLabel = new JLabel("Semester");
        final JTextField name = new JTextField(),
                units = new JTextField();
        final JComboBox<Integer> credits = new JComboBox<>(creditArray),
                semester = new JComboBox<>(semesterArray);
        save = new JButton("Save");
        done = new JButton("Done");
        cancel = new JButton("Cancel");

        if (startingSelection != null) {
            course.setSelectedItem(startingSelection);
        }

        try {
            currentCourse = AllCourses.getInstance().get(course.getSelectedItem().toString());
        } catch (NoSuchFieldException ex) {
            currentCourse = AllCourses.getInstance().getElements().get(course.getSelectedIndex());
        }

        name.setText(currentCourse.getName());
        name.setCaretPosition(0);
        units.setText(String.valueOf(currentCourse.getUnits()));
        credits.setSelectedItem(currentCourse.getCredits());
        semester.setSelectedItem(currentCourse.getSemester());

        course.addKeyListener(doneListener);
        name.addKeyListener(doneListener);
        units.addKeyListener(doneListener);
        credits.addKeyListener(doneListener);
        semester.addKeyListener(doneListener);
        done.addKeyListener(doneListener);
        cancel.addKeyListener(doneListener);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editCourse(name.getText(), Integer.parseInt(credits.getSelectedItem().toString()), Integer.parseInt(units.getText()), Integer.parseInt(semester.getSelectedItem().toString()));
                } catch (NumberFormatException ex) {
                    new ErrorMessage(ex, "A number was entered wrong").createAndViewGUI();
                }
            }
        });
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editCourse(name.getText(), Integer.parseInt(credits.getSelectedItem().toString()), Integer.parseInt(units.getText()), Integer.parseInt(semester.getSelectedItem().toString()));
                    dispose();
                } catch (NumberFormatException ex) {
                    new ErrorMessage(ex, "A number was entered wrong").createAndViewGUI();
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        course.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                try {
                    currentCourse = AllCourses.getInstance().get(course.getSelectedItem().toString());
                } catch (NoSuchFieldException ex) {
                    currentCourse = AllCourses.getInstance().getElements().get(course.getSelectedIndex());
                }
                name.setText(currentCourse.getName());
                units.setText(String.valueOf(currentCourse.getUnits()));
                credits.setSelectedItem(currentCourse.getCredits());
                semester.setSelectedItem(currentCourse.getSemester());
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 8, 5, 8);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 3;
        layout.addLayoutComponent(course, constraints);
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        layout.addLayoutComponent(nameLabel, constraints);
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        layout.addLayoutComponent(name, constraints);
        constraints.gridy = 2;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        layout.addLayoutComponent(creditsLabel, constraints);
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        layout.addLayoutComponent(credits, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        layout.addLayoutComponent(unitsLabel, constraints);
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        layout.addLayoutComponent(units, constraints);
        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        layout.addLayoutComponent(semesterLabel, constraints);
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        layout.addLayoutComponent(semester, constraints);
        constraints.gridy = 5;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        layout.addLayoutComponent(save, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(done, constraints);
        constraints.gridx = 2;
        layout.addLayoutComponent(cancel, constraints);

        add(course);
        add(nameLabel);
        add(name);
        add(creditsLabel);
        add(credits);
        add(unitsLabel);
        add(units);
        add(semesterLabel);
        add(semester);
        add(save);
        add(done);
        add(cancel);

        setMinimumSize(new Dimension(250, 0));
    }

    @Override
    public void createAndViewGUI() {
        if (!AllCourses.getInstance().getElements().isEmpty()) {
            super.createAndViewGUI();
        } else {
            new ErrorMessage(new NoSuchFieldException("No courses"), "No courses to edit").createAndViewGUI();
        }
    }
}
