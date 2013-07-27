package org.carroll.dialogs.editing;

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
import org.carroll.data.settings.Settings;
import org.swing.Dialog;
import org.swing.Listeners;
import org.swing.dialogs.messages.ErrorMessage;

/**
 * Dialog meant to edit settings.
 *
 * @author Joel Gallant
 */
public class SettingsDialog extends Dialog {

    JButton done, cancel;
    KeyListener doneListener = new Listeners.DoneListener() {
        @Override
        public void enterPressed() {
            done.doClick();
        }
    };

    void setNewValues(String[] values) {
        try {
            Settings.getInstance().get("AutoSaveOn").set(values[0]);
            Settings.getInstance().get("AutoSaveDelay").set(values[1]);
            Settings.getInstance().get("Semesters").set(values[2]);
        } catch (NoSuchFieldException ex) {
            new ErrorMessage(ex, "No setting found under that name. Possible internal error.").createAndViewGUI();
        }
    }

    @Override
    protected void init() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        String[] semesters = {"1", "2", "4"};

        final JCheckBox autoSaveOn = new JCheckBox("Auto save on");
        JLabel autoSaveDelayLabel = new JLabel("Auto save delay");
        final JTextField autoSaveDelayField = new JTextField();
        JLabel semestersLabel = new JLabel("Semesters");
        final JComboBox<String> semestersSelector = new JComboBox<>(semesters);
        done = new JButton("Done");
        cancel = new JButton("Cancel");

        try {
            autoSaveOn.setSelected(Settings.getInstance().get("AutoSaveOn").getValue().equals("true"));
        } catch (NoSuchFieldException ex) {
            autoSaveOn.setSelected(true);
        }

        try {
            autoSaveDelayField.setText(Settings.getInstance().get("AutoSaveDelay").getValue());
        } catch (NoSuchFieldException ex) {
            autoSaveDelayField.setText("30");
        }

        try {
            semestersSelector.setSelectedItem(Settings.getInstance().get("Semesters").getValue());
        } catch (NoSuchFieldException ex) {
            semestersSelector.setSelectedItem("2");
        }

        autoSaveOn.addKeyListener(doneListener);
        autoSaveDelayField.addKeyListener(doneListener);
        semestersSelector.addKeyListener(doneListener);
        done.addKeyListener(doneListener);
        cancel.addKeyListener(doneListener);

        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] values = {
                    String.valueOf(autoSaveOn.isSelected()),
                    autoSaveDelayField.getText(),
                    semestersSelector.getSelectedItem().toString()
                };
                setNewValues(values);
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
        constraints.insets = new Insets(5, 8, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 2;
        layout.addLayoutComponent(autoSaveOn, constraints);
        constraints.gridwidth = 1;
        constraints.gridy = 1;
        layout.addLayoutComponent(autoSaveDelayLabel, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(autoSaveDelayField, constraints);
        constraints.gridy = 2;
        constraints.gridx = 0;
        layout.addLayoutComponent(semestersLabel, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(semestersSelector, constraints);
        constraints.gridy = 3;
        constraints.gridx = 0;
        layout.addLayoutComponent(done, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(cancel, constraints);

        add(autoSaveOn);
        add(autoSaveDelayLabel);
        add(autoSaveDelayField);
        add(semestersLabel);
        add(semestersSelector);
        add(done);
        add(cancel);
    }
}
