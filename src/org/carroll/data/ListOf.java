package org.carroll.data;

import java.util.ArrayList;

/**
 * Interface for classes that store an {@link ArrayList} of data.
 *
 * @param <E> Type of data
 * @author Joel Gallant
 */
public abstract class ListOf<E extends Comparable<E>> {

    ArrayList<E> elements;

    /**
     * Remove an item based on it's name.
     *
     * @param name name of the item to remove
     */
    public abstract void remove(String name);

    /**
     * Get the element by the name in the parameter.
     *
     * @param name name of the element
     * @return element with the name in the parameter
     * @throws NoSuchFieldException thrown when no field is found under that
     * name
     */
    public abstract E get(String name) throws NoSuchFieldException;
    
    public abstract void sort();

    /**
     * Returns an {@link ArrayList} of all the elements.
     *
     * @return array list of work
     */
    public synchronized ArrayList<E> getElements() {
        if (elements == null) {
            elements = new ArrayList<>();
        }
        return elements;
    }

    /**
     * Adds a new element to the list
     *
     * @param newElement new work
     */
    public void add(E newElement) {
        getElements().add(newElement);
    }

    /**
     * Removes an element from the list
     *
     * @param element element to remove
     */
    public void remove(E element) {
        getElements().remove(element);
    }

    /**
     * Removes all elements from the database.
     */
    public void removeAll() {
        getElements().clear();
    }
}
