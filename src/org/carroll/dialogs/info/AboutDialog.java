package org.carroll.dialogs.info;

import org.carroll.main.BishopCarrollSchoolTracker;
import org.swing.dialogs.MessageDialog;

/**
 * Dialog for displaying information about the program.
 *
 * @author Joel Gallant
 */
public class AboutDialog extends MessageDialog {

    private static final String about = "Version "+BishopCarrollSchoolTracker.getVersion()
            + "<br>This program was made with the intention </br>"
            + "<br>of creating a usable interface for Bishop Carroll students to manage </br>"
            + "<br>their schoolwork in a way that is specifically made for them.</br>"
            + "<br>The Bishop Carroll Schoolwork Tracker was developed during the "
            + "summer of 2012, by Joel Gallant."
            + "</br>"
            + "<br>If you have any questions about how to use this program, feel free "
            + "to email me at joelgallant236@gmail.com"
            + "</br>"
            + "<br>Thanks!</br>";

    /**
     * Creates new about dialog.
     */
    public AboutDialog() {
        super("About this program", about);
    }
}
