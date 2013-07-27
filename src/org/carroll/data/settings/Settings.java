package org.carroll.data.settings;

import java.util.ArrayList;
import org.carroll.data.ListOf;

/**
 * Stores all user settings.
 *
 * @author Joel Gallant
 */
public class Settings extends ListOf<Setting> {

    static Settings instance;

    Settings() {
    }

    /**
     * Returns a singleton instance of settings
     *
     * @return singleton instance
     */
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    @Override
    public void remove(String name) {
        for (Setting setting : getElements()) {
            if (setting.getName().equals(name)) {
                getElements().remove(setting);
            }
        }
    }

    @Override
    public Setting get(String name) throws NoSuchFieldException {
        for (Setting setting : getElements()) {
            if (setting.getName().equals(name)) {
                return setting;
            }
        }
        throw new NoSuchFieldException("No setting " + name + " found.");
    }

    @Override
    public void sort() {
        //No need
    }

    /**
     * Class that stores the default settings in ram.
     */
    public static class Defaults {

        static final ArrayList<Setting> settings = new ArrayList<>();

        /**
         * Returns all the default settings
         *
         * @return default settings
         */
        public static ArrayList<Setting> getSettings() {
            if (settings.isEmpty()) {
                Setting autoSaveOn = new Setting("AutoSaveOn"),
                        autoSaveDelay = new Setting("AutoSaveDelay"),
                        currentGrade = new Setting("CurrentGrade"),
                        semesters = new Setting("Semesters");
                autoSaveOn.set("true");
                autoSaveDelay.set("30");
                currentGrade.set("10");
                semesters.set("2");
                settings.add(autoSaveOn);
                settings.add(autoSaveDelay);
                settings.add(currentGrade);
                settings.add(semesters);
            }
            return settings;
        }
    }
}
