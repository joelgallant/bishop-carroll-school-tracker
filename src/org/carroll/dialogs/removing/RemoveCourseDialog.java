package org.carroll.dialogs.removing;

import org.carroll.data.school.AllCourses;
import org.carroll.internal.InternalPanel;
import org.carroll.utils.Logging;
import org.swing.dialogs.messages.ErrorMessage;
import org.swing.dialogs.removal.SelectableRemover;

/**
 * Dialog for removing courses.
 *
 * @author Joel Gallant
 */
public class RemoveCourseDialog extends SelectableRemover {

    @Override
    public void removeAt(int index) {
    }

    @Override
    public String[] getOptions() {
        return AllCourses.getInstance().getNames();
    }

    @Override
    public void remove(String name) {
        Logging.log("Removing course - " + name);
        AllCourses.getInstance().remove(name);
        InternalPanel.COURSES.refresh();
    }

    @Override
    public void createAndViewGUI() {
        if (AllCourses.getInstance().getElements().isEmpty()) {
            new ErrorMessage(new NoSuchFieldException("No Courses"), "No courses to remove").createAndViewGUI();
        } else {
            super.createAndViewGUI();
        }
    }
}
