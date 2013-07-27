package org.carroll.dialogs.info.tutorial;

import org.carroll.internal.InternalPanel;
import org.swing.dialogs.MessageDialog;

/**
 * Last step in the tutorial.
 *
 * @author Joel Gallant
 */
public class Conclusion extends MessageDialog {

    /**
     * Creates conclusion.
     */
    public Conclusion() {
        super("That's it!</br>"
                + "<br>"
                + "<br>Try playing around with the program for a</br>"
                + "<br>few minutes before you start putting all of your work in.</br>"
                + "<br>You don't want to use something you don't like. Some people</br>"
                + "<br>just prefer using a pencil and agenda to plan, and that's great!<br>"
                + "<br>"
                + "<br>Thank you either way for trying this program out! Hopefully it's of good use.</br>"
                + "<br>"
                + "<br>-Joel Gallant");
        InternalPanel.changePanelTo(InternalPanel.COURSES);
    }
}
