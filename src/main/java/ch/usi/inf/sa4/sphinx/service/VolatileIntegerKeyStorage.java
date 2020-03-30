package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Storable;


/**
 * This is a Volatile Storage for items that use an Integer key
 * @param <T> The type of the stored items
 */
public abstract class VolatileIntegerKeyStorage<T extends Storable<Integer, T>> extends VolatileStorage<Integer, T> {
    private Integer id;


    protected VolatileIntegerKeyStorage() {
        this.id = 1;
    }

    /**
     * @param item might be used to generate the key ex: the key for a certain items might be one of their fields
     * @return the generated key
     */
    @Override
    protected Integer generateKey(T item) {
        return id++;
    }
}
