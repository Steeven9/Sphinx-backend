package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Storable;

public abstract class Storage<K, T extends Storable<K> > {
    /**
     * Retrieves an item its key
     *
     * @param key the key for the requested item
     * @return the requested room or null if no room with that id exists.
     */
     protected  abstract T get(final K key);


    /**
     * Inserts a copy of the given item into storage.
     *
     * @param item the item to insert
     * @return The key of the created item, null if it fails
     */
    protected  abstract K insert(final T item);


    /**
     * Deletes the Device with the given Id. Has no effect if no such Device exists.
     *
     * @param key the id of the Device to delete
     */
    protected abstract void delete(final K key);


    /**
     * Updates the Item with the given key
     * @param updatedItem the item with updated fields
     * @return true if the operation is successful else false
     */
    protected  abstract boolean update(final T updatedItem);

}
