package org.carroll.dialogs.info.tutorial;

import org.carroll.dialogs.MainFrame;
import org.carroll.internal.InternalPanel;
import org.swing.dialogs.StepDialog;

/**
 * Step 2 in the tutorial.
 *
 * @author Joel Gallant
 */
public class Step2 extends StepDialog {

    /**
     * Creates step 2.
     */
    public Step2() {
        super("This is the Courses panel</br>"
                + "<br>"
                + "<br>This is where all the courses you are doing for the year are displayed.</br>"
                + "<br>To add your course(s), click on the Add button from the Courses panel screen.</br>"
                + "<br>You can also add your course(s) by clicking on Edit>Courses>Add Course</br>"
                + "<br>from the top down menu, or press Alt+N.</br>"
                + "<br>"
                + "<br>If you want to import courses from one of our</br>"
                + "<br>pre-made lists, they are available through the project's sourceforge page.</br>"
                + "<br>(This link is also available in README.txt file in your installation folder).</br>"
                + "<br>"
                + "<br>Follow the download and installation instructions in the README file.</br>", MainFrame.getInstance());
        InternalPanel.changePanelTo(InternalPanel.COURSES);
    }

    /**
     * Goes to next step.
     */
    @Override
    protected void next() {
        new Step3().createAndViewGUI();
    }
}
