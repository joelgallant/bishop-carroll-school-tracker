package org.carroll.dialogs.info.tutorial;

import org.carroll.dialogs.MainFrame;
import org.carroll.internal.InternalPanel;
import org.swing.dialogs.MessageDialog;
import org.swing.dialogs.StepDialog;

/**
 * Step 1 in the tutorial.
 *
 * @author Joel Gallant
 */
public class Step1 extends StepDialog {

    /**
     * Creates step 1.
     */
    public Step1() {
        super("This is the To Do panel.</br>"
                + "<br>"
                + "<br>Your To Do list will automatically populate according to </br>"
                + "<br>the information you’ve inputed (Courses, Work, and Workload).</br>"
                + "<br>Stats on the right will automatically update when you enter the panel.</br>"
                + "<br>"
                + "<br>If the information appears outdated, click on the To Do button to</br>"
                + "<br>refresh the screen.", MainFrame.getInstance());
        InternalPanel.changePanelTo(InternalPanel.TODO);
    }

    /**
     * Goes to next step.
     */
    @Override
    protected void next() {
        new Step2().createAndViewGUI();
    }
}
