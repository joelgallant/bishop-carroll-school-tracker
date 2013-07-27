package org.carroll.dialogs.info.tutorial;

import org.carroll.dialogs.MainFrame;
import org.carroll.internal.InternalPanel;
import org.swing.dialogs.StepDialog;

/**
 * Step 4 in the tutorial.
 *
 * @author Joel Gallant
 */
public class Step4 extends StepDialog {

    /**
     * Creates step 4.
     */
    public Step4() {
        super("This is the Workload panel.</br>"
                + "<br>In this panel, you can adjust how much work you plan to do each month.</br>"
                + "<br>The amount of units on the right determines how much work to allocate</br>"
                + "<br>in the ‘To Do’ panel in that month.</br>", MainFrame.getInstance());
        InternalPanel.changePanelTo(InternalPanel.WORKLOAD);
    }

    /**
     * Goes to next step.
     */
    @Override
    protected void next() {
        new Step5().createAndViewGUI();
    }
}
