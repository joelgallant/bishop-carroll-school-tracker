package org.carroll.dialogs.info.tutorial;

/**
 * Class that shows the user how to use the program.
 *
 * @author Joel Gallant
 */
public class Tutorial {

    /**
     * Starts the tutorial for the user.
     */
    public static void startTutorial() {
        new Step1().createAndViewGUI();
    }
}
