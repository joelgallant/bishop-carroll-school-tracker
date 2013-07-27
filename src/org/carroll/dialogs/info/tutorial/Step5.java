package org.carroll.dialogs.info.tutorial;

import org.carroll.dialogs.MainFrame;
import org.carroll.dialogs.editing.SettingsDialog;
import org.swing.dialogs.StepDialog;

/**
 * Step 5 in the tutorial.
 *
 * @author Joel Gallant
 */
public class Step5 extends StepDialog {

    SettingsDialog s;

    /**
     * Creates step 5.
     */
    public Step5() {
        super("This is the Settings dialog window.</br>"
                + "<br>If you want to adjust the amount of semesters, </br>"
                + "<br>or want to change the auto-saving settings, you go here.</br>"
                + "<br>To change your current grade level from the Grade 10 default, </br>"
                + "<br>click on Edit>Current Grade.", MainFrame.getInstance());
        s = new SettingsDialog();
        s.createAndViewGUI();
        s.setAlwaysOnTop(true);
    }

    @Override
    public void createAndViewGUI() {
        super.createAndViewGUI();
        setAlwaysOnTop(false);
        s.setLocation(this.getX() + 100, this.getY() - 200);
    }

    /**
     *
     */
    @Override
    protected void next() {
        s.dispose();
        new Conclusion().createAndViewGUI();
    }
}
