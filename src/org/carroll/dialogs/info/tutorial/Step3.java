package org.carroll.dialogs.info.tutorial;

import org.carroll.dialogs.MainFrame;
import org.carroll.internal.InternalPanel;
import org.swing.dialogs.StepDialog;

/**
 * Step 4 of the tutorial.
 *
 * @author Joel Gallant
 */
public class Step3 extends StepDialog {

    /**
     * Creates step 3.
     */
    public Step3() {
        super("This is the Work panel</br>"
                + "<br>"
                + "<br>This is where all your work for the year is displayed.</br>"
                + "<br>"
                + "<br>To add your work, click on the Add button from the Work panel screen.</br>"
                + "<br>You can also add your course(s) by clicking on </br>"
                + "<br>Edit>Work>Add Work from the top down menu.", MainFrame.getInstance());
        InternalPanel.changePanelTo(InternalPanel.WORK);
    }

    /**
     * Goes to next step.
     */
    @Override
    protected void next() {
        new Step4().createAndViewGUI();
    }
}
