package org.carroll.internal;

import java.awt.Color;
import javax.swing.JPanel;
import org.carroll.dialogs.MainFrame;
import org.carroll.internal.panels.CoursesPanel;
import org.carroll.internal.panels.ToDoPanel;
import org.carroll.internal.panels.WorkPanel;
import org.carroll.internal.panels.WorkloadPanel;
import org.carroll.utils.Logging;
import org.swing.Refreshable;

/**
 * Internal panel - contains the 4 panels inside of the internal panel, and is
 * able to switch between them.
 *
 * @author Joel Gallant
 */
public abstract class InternalPanel extends JPanel implements Refreshable {

    static final Color defaultColor = Color.GRAY,
            highlighted = Color.LIGHT_GRAY;

    /**
     * To do panel - displays data about what the user should do in the upcoming
     * week / month.
     */
    public static final ToDoPanel TODO = new ToDoPanel();

    /**
     * Work panel - displays data about what work the user needs to do.
     */
    public static final WorkPanel WORK = new WorkPanel();

    /**
     * Courses panel - displays data about what courses the user is doing.
     */
    public static final CoursesPanel COURSES = new CoursesPanel();

    /**
     * Workload panel - displays data about how much work the user is doing per
     * month.
     */
    public static final WorkloadPanel WORKLOAD = new WorkloadPanel();

    /**
     * Changes the internal panel to a different panel. Instances of the
     * internal panels are available in {@link InternalPanel}.
     *
     * @param panel new panel to change display to
     */
    public static void changePanelTo(InternalPanel panel) {
        Logging.log("Changing panel to " + panel.getClass().getSimpleName());
        TODO.setVisible(panel == TODO);
        WORK.setVisible(panel == WORK);
        COURSES.setVisible(panel == COURSES);
        WORKLOAD.setVisible(panel == WORKLOAD);
        if(panel == TODO) {
            MainFrame.switchButton("todo");
        }else if(panel == WORK) {
            MainFrame.switchButton("work");
        }else if(panel == COURSES) {
            MainFrame.switchButton("courses");
        }else if(panel == WORKLOAD) {
            MainFrame.switchButton("workload");
        }
        panel.refresh();
    }
}