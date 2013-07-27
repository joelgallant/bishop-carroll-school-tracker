package org.carroll.data;

import org.carroll.data.settings.Settings;

/**
 * Class that handles where files are located.
 *
 * @author Joel Gallant
 */
public class Paths {

    static String main = System.getProperty("user.home") + "/Bishop Carroll Tracker/";

    /**
     * Returns the path of the main configuration folder
     *
     * @return main config folder path
     */
    public static String getMain() {
        return main;
    }

    /**
     * Returns the path of the current grade directory
     *
     * @return path of grade directory
     */
    public static String getGrade() {
        try {
            return getMain() + "Grade " + Settings.getInstance().get("CurrentGrade").getValue() + "/";
        } catch (NoSuchFieldException ex) {
            return getMain() + "DEFAULT/";
        }
    }

    /**
     * Returns the path of the log file
     *
     * @return log file path
     */
    public static String getLog() {
        return getMain() + "log.txt";
    }

    /**
     * Returns the path of the config file
     *
     * @return config file path
     */
    public static String getConfig() {
        return getMain() + "config.txt";
    }

    /**
     * Sets the main configuration folder's path
     *
     * @param path new path for the main folder
     */
    public static void setMain(String path) {
        main = path;
    }
}
