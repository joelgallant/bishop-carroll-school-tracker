package org.carroll.data.settings;

/**
 * Standard setting class.
 *
 * @author Joel Gallant
 */
public class Setting implements Comparable<Setting> {

    String name, value;

    /**
     * Creates the setting by the name given in the parameter.
     *
     * @param name name of the setting
     */
    public Setting(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the setting.
     *
     * @return name of the setting
     */
    public String getName() {
        return name;
    }

    /**
     * Returns what value the setting has been assigned.
     *
     * @return value of setting
     */
    public String getValue() {
        return value == null ? "" : value;
    }

    /**
     * Returns the string representation of the setting. Includes the name and
     * value.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return name + " - " + value;
    }

    /**
     * Creates a new identical setting to the old one, with the same value.
     *
     * @return new identical setting
     */
    @Override
    protected Setting clone() {
        Setting n = new Setting(this.getName());
        n.set(this.getValue());
        return n;
    }

    /**
     * Tests to see if the two settings are of the same value.
     *
     * @param obj other setting
     * @return whether the two settings contain the same value
     */
    public boolean equals(Setting obj) {
        return this.getValue().equals(obj.getValue());
    }

    /**
     * Sets the setting's value.
     *
     * @param value new value of the setting
     */
    public void set(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(Setting o) {
        return this.getValue().compareTo(o.getValue());
    }
}
