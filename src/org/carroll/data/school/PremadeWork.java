package org.carroll.data.school;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.table.TableModel;
import org.carroll.data.Paths;
import org.carroll.school.Work;
import org.carroll.utils.Excel;
import org.carroll.utils.Logging;

/**
 * Class that contains all pre-made work stored in /premade.
 *
 * @author Joel Gallant
 */
public class PremadeWork {

    final static TableModel[] premade;
    final static ArrayList<Work> work = new ArrayList<>();

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
                    if (premade[index].getValueAt(row, 5) != null && premade[index].getValueAt(row, 5) != "") {
                        Work newWork = new Work(premade[index].getValueAt(row, 5).toString()) {
                            @Override
                            protected String name() {
                                return premade[index].getValueAt(row, 3).toString();
                            }

                            @Override
                            protected int units() {
                                return Integer.parseInt(premade[index].getValueAt(row, 4).toString());
                            }

                            @Override
                            protected int order() {
                                return 0;
                            }

                            @Override
                            protected int weight() {
                                return Integer.parseInt(premade[index].getValueAt(row, 6).toString());
                            }
                        };
                        work.add(newWork);
                    }
                } catch (Exception ex) {
                    // No work existed there.
                }
            }
        }
    }

    /**
     * Returns the work of a certain course from the pre-made work.
     *
     * @param courseName course
     * @return list of work from that course
     */
    public static ArrayList<Work> getWorkOf(String courseName) {
        ArrayList<Work> workOf = new ArrayList<>();
        int order = 1;
        for (Work x : work) {
            if (x.getCourseName().equals(courseName)) {
                x.setOrder(order);
                workOf.add(x);
                order++;
            }
        }
        return workOf;
    }
}
