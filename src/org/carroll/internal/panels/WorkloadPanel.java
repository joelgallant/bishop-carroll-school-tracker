package org.carroll.internal.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.carroll.data.school.Workload;
import org.carroll.internal.InternalPanel;

/**
 * Workload panel - displays data about how much work the user is doing per
 * month.
 *
 * @author Joel Gallant
 */
public class WorkloadPanel extends InternalPanel {

    JLabel[] units;

    /**
     * Creates the workload panel.
     */
    public WorkloadPanel() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        JLabel[] months = {
            new JLabel("Month"), new JLabel("September"), new JLabel("October"), new JLabel("November"),
            new JLabel("December"), new JLabel("January"), new JLabel("February"), new JLabel("March"),
            new JLabel("April"), new JLabel("May"), new JLabel("June")
        };
        final JSlider[] sliders = new JSlider[10];
        JLabel unitsLabel = new JLabel("Units Left");
        units = new JLabel[10];

        months[0].setFont(months[0].getFont().deriveFont(Font.BOLD));
        for (int x = 0; x < sliders.length; x++) {
            sliders[x] = new JSlider();
        }
        for (int x = 0; x < units.length; x++) {
            units[x] = new JLabel();
        }

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        for (int x = 0; x < months.length; x++) {
            months[x].setFont(months[x].getFont().deriveFont((float) 24));
            months[x].setHorizontalAlignment(SwingConstants.CENTER);
            layout.addLayoutComponent(months[x], constraints);
            add(months[x]);
            constraints.gridy++;
        }

        constraints.gridx = 1;
        constraints.gridy = 1;
        for (int x = 0; x < sliders.length; x++) {
            final int slider = x;
            sliders[x].addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    Workload.setWorkload(slider, sliders[slider].getValue());
                    refreshUnits();
                }
            });
            sliders[x].setValue(Workload.getWorkload(slider));
            layout.addLayoutComponent(sliders[x], constraints);
            add(sliders[x]);
            constraints.gridy++;
        }
        constraints.gridy = 0;
        constraints.gridx = 2;
        unitsLabel.setFont(unitsLabel.getFont().deriveFont(Font.BOLD, (float)24));
        unitsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layout.addLayoutComponent(unitsLabel, constraints);
        add(unitsLabel);

        constraints.gridy = 1;
        for (int x = 0; x < units.length; x++) {
            units[x].setFont(units[x].getFont().deriveFont((float) 24));
            units[x].setHorizontalAlignment(SwingConstants.CENTER);
            layout.addLayoutComponent(units[x], constraints);
            add(units[x]);
            constraints.gridy++;
        }
    }

    @Override
    public void refresh() {
        refreshUnits();
    }

    void refreshUnits() {
        for (int x = 0; x < units.length; x++) {
            units[x].setText(String.valueOf(Workload.getUnits(x)));
        }
    }
}
