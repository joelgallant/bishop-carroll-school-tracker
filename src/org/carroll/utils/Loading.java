package org.carroll.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.carroll.data.Paths;
import org.carroll.data.school.AllCourses;
import org.carroll.data.school.AllWork;
import org.carroll.data.school.Workload;
import org.carroll.data.settings.Setting;
import org.carroll.data.settings.Settings;
import org.carroll.internal.InternalPanel;
import org.carroll.school.Course;
import org.carroll.school.Work;

/**
 * Main class that takes care of loading previous configurations from files.
 *
 * @author Joel Gallant
 */
public class Loading {

    /**
     * Loads everything required from saved files.
     */
    public static void loadEverything() {
        loadSettings();
        loadData();
        loadDefaults();

        // KEEP THESE AT THE BOTTOM. CALLING THEM CREATES ALL STATIC OBJECTS IN INTERNAL PANEL AND AND INITS DATA INSIDE
        InternalPanel.COURSES.refresh();
        InternalPanel.WORK.refresh();
        InternalPanel.WORKLOAD.refresh();
        InternalPanel.TODO.refresh();
    }

    /**
     * Loads all saved settings.
     */
    public static void loadSettings() {
        try {
            String allSettings = Files.TextFiles.getStringFromFile(new File(Paths.getConfig()));
            String[] lines = allSettings.split(System.getProperty("line.separator"));
            for (String line : lines) {
                if (!line.isEmpty()) {
                    String[] setting = line.split(" = ");
                    String settingName = setting[0].trim();
                    String settingValue = setting[setting.length - 1].trim();
                    try {
                        Settings.getInstance().get(settingName).set(settingValue);
                    } catch (NoSuchFieldException ex) {
                        Setting newSetting = new Setting(settingName);
                        newSetting.set(settingValue);
                        Settings.getInstance().add(newSetting);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logging.log("No settings file found!");
        }
    }

    /**
     * Loads all data from files.
     */
    public static void loadData() {
        try {
            AllCourses.getInstance().removeAll();
            ArrayList<Course> courses = Excel.getCoursesFromFile(new File(Paths.getGrade() + "courses.xls"));
            for (int x = 0; x < courses.size(); x++) {
                AllCourses.getInstance().add(courses.get(x));
            }
        } catch (IOException ex) {
            Logging.log("Unable to load courses from file");
        }
        try {
            AllWork.getInstance().removeAll();
            ArrayList<Work> work = Excel.getWorkFromFile(new File(Paths.getGrade() + "work.xls"));
            for (int x = 0; x < work.size(); x++) {
                AllWork.getInstance().add(work.get(x));
            }
        } catch (IOException ex) {
            Logging.log("Unable to load work from file");
        }
        try {
            DefaultTableModel model = (DefaultTableModel) Excel.getTableModelFrom(new File(
                    Paths.getGrade() + "workload.xls"));
            for (int x = 0; x < model.getRowCount(); x++) {
                Workload.setWorkload(x, Integer.parseInt(model.getValueAt(x, 0).toString()));
            }
        } catch (IOException ex) {
            Logging.log("Unable to load workload from file");
        }
    }

    /**
     * Loads all missing data that is missing from saved data.
     */
    public static void loadDefaults() {
        for (Setting setting : Settings.Defaults.getSettings()) {
            try {
                if (Settings.getInstance().get(setting.getName()).getValue().equals("")) {
                    Settings.getInstance().get(setting.getName()).set(setting.getValue());
                }
            } catch (NoSuchFieldException ex) {
                Settings.getInstance().add(setting);
            }
        }
        File premadeFolder = new File(Paths.getMain()+"premade/");
        if(!premadeFolder.exists()) {
            premadeFolder.mkdirs();
        }
    }
}
