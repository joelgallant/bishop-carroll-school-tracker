package org.carroll.data.school;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.table.TableModel;
import org.carroll.data.Paths;
import org.carroll.utils.Excel;
import org.carroll.utils.Logging;
import org.carroll.school.Course;

/**
 * Class that contains all pre-made courses.
 *
 * @author Joel Gallant
 */
public class PremadeCourses {

    final static TableModel[] premade;
    final static ArrayList<Course> courses = new ArrayList<>();
    final static String[] names;

    static {
        File premadeFiles[] = new File(Paths.getMain() + "premade/").listFiles();
        premade = new TableModel[premadeFiles.length];
        for (int x = 0; x < premadeFiles.length; x++) {
            try {
                premade[x] = Excel.getTableModelFrom(premadeFiles[x]);
            } catch (IOException ex) {
                Logging.log("Could not import premade file - " + premadeFiles[x].getPath());
            }
        }

        for (int x = 0; x < premade.length; x++) {
            final int index = x;
            for (int i = 0; i < (premade[x] != null ? premade[x].getRowCount() : 0); i++) {
                final int row = i;
                try {
                    if (premade[index].getValueAt(row, 0) != null && premade[index].getValueAt(row, 0) != "") {
                        Course newCourse = new Course() {
                            @Override
                            protected String name() {
                                return premade[index].getValueAt(row, 0).toString();
                            }

                            @Override
                            protected int credits() {
                                return Integer.parseInt(premade[index].getValueAt(row, 1).toString());
                            }

                            @Override
                            protected int semester() {
                                return 1;
                            }
                        };
                        courses.add(newCourse);
                    }
                } catch (Exception ex) {
                    //No course existed there.
                }
            }
        }

        names = new String[courses.size() + 1];
        names[0] = "Pre-Made courses";
        for (int x = 0; x < courses.size(); x++) {
            names[x + 1] = courses.get(x).getName();
        }
    }

    /**
     * Returns an array of the course's names.
     *
     * @return array of strings of course names
     */
    public static String[] getCourseNames() {
        return names;
    }

    /**
     * Returns an {@link ArrayList} of the courses in the pre-made files.
     *
     * @return courses in pre-made files
     */
    public static ArrayList<Course> getCourses() {
        return courses;
    }
}
