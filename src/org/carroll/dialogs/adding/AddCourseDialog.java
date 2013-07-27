package org.carroll.dialogs.adding;

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
import org.carroll.data.school.AllWork;
import org.carroll.data.school.PremadeCourses;
import org.carroll.data.school.PremadeWork;
import org.carroll.data.settings.Settings;
import org.carroll.internal.InternalPanel;
import org.carroll.utils.Logging;
import org.carroll.school.Course;
import org.carroll.school.Work;
import org.joda.time.IllegalFieldValueException;
import org.swing.Dialog;
import org.swing.Listeners;
import org.swing.dialogs.messages.ErrorMessage;

/**
 * Dialog that allows the user to add a new course.
 *
 * @author Joel Gallant
 */
public class AddCourseDialog extends Dialog {

    JButton done, cancel;
    KeyListener doneListener = new Listeners.DoneListener() {
        @Override
        public void enterPressed() {
            done.doClick();
        }
    };

    /**
     * Creates new course dialog.
     */
    public AddCourseDialog() {
        super("Add Course");
    }

    void addCourse(final String name, final int credits, final int semester) {
        Logging.log("Adding new course - " + name);
        AllCourses.getInstance().add(new Course() {
            @Override
            protected String name() {
                return name;
            }

            @Override
            protected int credits() {
                return credits;
            }

            @Override
            protected int semester() {
                return semester;
            }
        });
        InternalPanel.COURSES.refresh();
    }

    @Override
    protected void init() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        setMinimumSize(new Dimension(250, 0));

        Integer[] creditArray = {5, 3, 1};
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

        final JComboBox<String> premadeCourses = new JComboBox<>(PremadeCourses.getCourseNames());
        JLabel nameLabel = new JLabel("Name"),
                creditsLabel = new JLabel("Credits"),
                semesterLabel = new JLabel("Semester");
        final JTextField nameField = new JTextField();
        final JComboBox<Integer> credits = new JComboBox<>(creditArray),
                semester = new JComboBox<>(semesterArray);
        done = new JButton("Add");
        cancel = new JButton("Cancel");

        premadeCourses.addKeyListener(doneListener);
        nameField.addKeyListener(doneListener);
        credits.addKeyListener(doneListener);
        semester.addKeyListener(doneListener);
        done.addKeyListener(doneListener);
        cancel.addKeyListener(doneListener);

        premadeCourses.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (premadeCourses.getSelectedIndex() != 0) {
                    nameField.setText(PremadeCourses.getCourses().get(premadeCourses.getSelectedIndex() - 1).getName());
                    credits.setSelectedItem(PremadeCourses.getCourses().get(premadeCourses.getSelectedIndex() - 1).getCredits());
                }
            }
        });
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AllCourses.getInstance().get(nameField.getText());
                    new ErrorMessage(new IllegalFieldValueException("Name", nameField.getText()), "Name already exists").createAndViewGUI();
                } catch (NoSuchFieldException ex) {
                    if (premadeCourses.getSelectedIndex() != 0) {
                        for (Work work : PremadeWork.getWorkOf(premadeCourses.getSelectedItem().toString())) {
                            AllWork.getInstance().add(work);
                        }
                    }
                    try {
                        addCourse(nameField.getText(), Integer.parseInt(credits.getSelectedItem().toString()), Integer.parseInt(semester.getSelectedItem().toString()));
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
        layout.addLayoutComponent(premadeCourses, constraints);
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        layout.addLayoutComponent(nameLabel, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(nameField, constraints);
        constraints.gridy = 2;
        constraints.gridx = 0;
        layout.addLayoutComponent(creditsLabel, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(credits, constraints);
        constraints.gridy = 3;
        constraints.gridx = 0;
        layout.addLayoutComponent(semesterLabel, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(semester, constraints);
        constraints.gridy = 4;
        constraints.gridx = 0;
        layout.addLayoutComponent(done, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(cancel, constraints);

        add(premadeCourses);
        add(nameLabel);
        add(nameField);
        add(creditsLabel);
        add(credits);
        add(semesterLabel);
        add(semester);
        add(done);
        add(cancel);

    }
}
