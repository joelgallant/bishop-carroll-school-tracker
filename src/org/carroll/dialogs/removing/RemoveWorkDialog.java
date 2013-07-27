package org.carroll.dialogs.removing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import org.carroll.data.school.AllCourses;
import org.carroll.data.school.AllWork;
import org.carroll.internal.InternalPanel;
import org.carroll.school.Work;
import org.carroll.utils.Logging;
import org.swing.Listeners;
import org.swing.dialogs.RemoveDialog;
import org.swing.dialogs.messages.ErrorMessage;

/**
 * Dialog for removing work.
 *
 * @author Joel Gallant
 */
public class RemoveWorkDialog extends RemoveDialog {

    JButton done, cancel;
    KeyListener doneListener = new Listeners.DoneListener() {
        @Override
        public void enterPressed() {
            done.doClick();
        }
    };

    @Override
    public String[] getOptions() {
        return AllWork.getInstance().getNames();
    }

    @Override
    public void remove(String name) {
        Logging.log("Removing work - " + name);
        AllWork.getInstance().remove(name);
        InternalPanel.WORK.refresh();
    }

    void remove(String name, String course) {
        if (course.equals("All")) {
            remove(name);
            return;
        }
        Logging.log("Removing work - " + name + " in course " + course);
        AllWork.getInstance().remove(name, course);
        InternalPanel.WORK.refresh();
    }

    @Override
    protected void init() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        String[] courses = new String[AllCourses.getInstance().getNames().length + 1];
        courses[0] = "All";
        System.arraycopy(AllCourses.getInstance().getNames(), 0, courses, 1, AllCourses.getInstance().getNames().length);

        final JComboBox<String> course = new JComboBox<>(courses),
                work = new JComboBox<>(AllWork.getInstance().getNames());
        done = new JButton("Remove");
        cancel = new JButton("Cancel");

        course.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                work.removeAllItems();
                if (!course.getSelectedItem().equals("All")) {
                    for (Work x : AllWork.getInstance().getElements()) {
                        if (x.getCourseName().equals(course.getSelectedItem().toString())) {
                            work.addItem(x.getName());
                        }
                    }
                } else {
                    work.setModel(new DefaultComboBoxModel<>(AllWork.getInstance().getNames()));
                }
            }
        });
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(work.getSelectedItem().toString(), course.getSelectedItem().toString());
                dispose();
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
        constraints.gridwidth = 2;
        layout.addLayoutComponent(course, constraints);
        constraints.gridy = 1;
        layout.addLayoutComponent(work, constraints);
        constraints.gridwidth = 1;
        constraints.gridy = 2;
        layout.addLayoutComponent(done, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(cancel, constraints);

        add(course);
        add(work);
        add(done);
        add(cancel);
    }

    @Override
    public void createAndViewGUI() {
        if (AllWork.getInstance().getElements().isEmpty()) {
            new ErrorMessage(new NoSuchFieldException("No work"), "No work exists to remove").createAndViewGUI();
        } else {
            super.createAndViewGUI();
        }
    }
}
