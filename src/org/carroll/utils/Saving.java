package org.carroll.utils;

import java.io.File;
import java.io.IOException;
import org.carroll.data.Paths;
import org.carroll.data.settings.Setting;
import org.carroll.data.settings.Settings;
import org.main.Timer;

/**
 * Main saving class that takes care of saving everything.
 *
 * @author Joel Gallant
 */
public class Saving {

    /**
     * Saves everything that the user has done so far.
     */
    public static void saveEverything() {
        Logging.log("Saving everything");
        saveSettings();
        saveData();
    }

    /**
     * Saves all the settings.
     */
    public static void saveSettings() {
        String settings = "";
        for (Setting setting : Settings.getInstance().getElements()) {
            settings += setting.getName() + " = " + setting.getValue();
            settings += System.getProperty("line.separator");
        }
        try {
            Files.TextFiles.setStringAsFile(new File(Paths.getConfig()), settings);
        } catch (IOException ex) {
            Logging.log("Could not save settings to file. IO Exception");
        }
    }

    /**
     * Saves all the inputed data (Courses, Work, Workload, etc.)
     */
    public static void saveData() {
        try {
            Excel.saveCoursesToFile(new File(Paths.getGrade() + "courses.xls"));
            Excel.saveWorkToFile(new File(Paths.getGrade() + "work.xls"));
            Excel.saveWorkloadToFile(new File(Paths.getGrade()+"workload.xls"));
        } catch (IOException ex) {
            Logging.log("SAVING FAILED - COULD NOT EXPORT TO FILE");
        }
    }

    /**
     * Auto saving class. Saves everything periodically.
     */
    public static class AutoSaving implements Runnable {

        Thread thread;

        /**
         * Starts the auto saving thread.
         */
        public void start() {
            if (thread == null) {
                thread = new Thread(this, "Auto save worker");
            }
            thread.start();
        }

        /**
         * Runnable method that saves everything periodically.
         */
        @Override
        public void run() {
            boolean autoSaveOn;
            int savingDelay;
            while (true) {
                try {
                    autoSaveOn = Settings.getInstance().get("AutoSaveOn").getValue().equals("true");
                    savingDelay = Integer.parseInt(Settings.getInstance().get("AutoSaveDelay").getValue());
                } catch (NoSuchFieldException | NumberFormatException ex) {
                    autoSaveOn = true;
                    savingDelay = 30;
                }
                if (autoSaveOn) {
                    Saving.saveEverything();
                    Timer.delay(savingDelay - 1);
                }
                Timer.delay(1);
            }
        }
    }
}
