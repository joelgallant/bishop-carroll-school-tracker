package org.carroll.dialogs.info;

import org.swing.dialogs.MessageDialog;

/**
 * Dialog to display information about the requirements for highschool.
 *
 * @author Joel Gallant
 */
public class HighSchoolRequirementDialog extends MessageDialog {

    private static final String message = "<br>These are the minimum requirements for high school in Alberta:</br>"
            + "<br></br>"
            + "<br><b>1. Core Courses: Fifty-six credits must be comprised of the following and their prerequisites:</b></br>"
            + "<br>• English Language Arts – 30-1 or 30-2 (15 credits)</br>"
            + "<br>• Social Studies – 30 or 33 (15 credits)</br> "
            + "<br>• Mathematics – Pure 20, Applied 20, or 24 (10 credits)</br>"
            + "<br>• Science – 20 or 24 or Biology 20, Chemistry 20, or Physics 20 (10 credits)</br>"
            + "<br>• Physical Education 10 (3 credits)</br>"
            + "<br>• Career and Life Management (3 credits) </br>"
            + "<br></br>"
            + "<br><b>2. Optional Courses: Ten credits in any combination must be from the following:</b></br>"
            + "<br>• Career and Technology Studies (CTS)</br>"
            + "<br>• Fine Arts</br>"
            + "<br>• Second Languages (maximum 25 credits)</br>"
            + "<br>• Physical Education 20 and/or 30</br>"
            + "<br>• Locally developed and/or acquired and locally authorized courses in "
            + "CTS, fine arts, or second languages, Knowledge & Employability or IOP</br>"
            + "<br>• Knowledge & Employability or IOP occupational courses</br>"
            + "<br>• Registered Apprenticeship Program</br>"
            + "<br></br>"
            + "<br><b>3. Other Courses: Ten credits in any 30-level course (in addition "
            + "to English Language Arts and Social Studies) must be from the following:</b></br>"
            + "<br>• 35-level locally developed and/or acquired and locally authorized courses</br>"
            + "<br>• Career and Technology Studies, Advanced Level 3000 series</br>"
            + "<br>• 35-level Work Experience (maximum 15 credits)</br>"
            + "<br>• 30-4 level Knowledge & Employability courses or 36-level IOP courses</br>"
            + "<br>• 35-level Registered Apprenticeship Program</br>"
            + "<br>• 30-level Green Certificate Specialization</br>";

    /**
     * Creates new high school requirements dialog
     */
    public HighSchoolRequirementDialog() {
        super("Minimum Requirements for Highschool", message);
    }
}
