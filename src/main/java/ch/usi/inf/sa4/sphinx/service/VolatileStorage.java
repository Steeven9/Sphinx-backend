package ch.usi.inf.sa4.sphinx.service;


import ch.usi.inf.sa4.sphinx.misc.ImproperImplementationException;
import ch.usi.inf.sa4.sphinx.model.Storable;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;


public abstract class VolatileStorage<K, T extends Storable<K, T>> extends Storage<K, T> {
    protected Map<K, T> data;

    protected VolatileStorage() {
        data = new HashMap<>();
    }


    /**
     *  Generates a key, can use {@param item} to generate it
     * @param item an item of the given type
     * @return the generated key
     */
    protected abstract K generateKey(@NotNull T item);


    /**
     * {@inheritDoc}
     */
    @Override
    protected T get(K key) {
        T storageItem = data.get(key);
        if(storageItem == null ) return  null;

        T returnItem = storageItem.makeCopy();
        returnItem.lockKey();
        return returnItem;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected K insert(@NotNull T item) {
        K key = generateKey(item);
        if (key == null) return null;

        if (item.setKey(key)) {
            T storageItem = item.makeCopy();

            if (storageItem == item) {
                String className = item.getClass().getName();
                throw new ImproperImplementationException(className + ".makeCopy() should not reference the same object");
            }

            data.put(key, item);
            return key;
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void delete(K key) {
        data.remove(key);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean update(@NotNull T updatedItem) {
        K key = updatedItem.getKey();
        if (key == null || !data.containsKey(key)) {
            return false;
        }

        T storageItem = updatedItem.makeCopy();
        if (storageItem == updatedItem || storageItem == null) {
            String className = updatedItem.getClass().getName();
            throw new ImproperImplementationException(className + ".makeCopy() should not reference the same object or null");
        }

        data.put(key, updatedItem);
        return true;
    }


}