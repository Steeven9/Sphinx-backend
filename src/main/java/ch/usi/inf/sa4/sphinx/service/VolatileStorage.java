package ch.usi.inf.sa4.sphinx.service;


import ch.usi.inf.sa4.sphinx.model.Storable;

import java.util.HashMap;
import java.util.Map;

public abstract class VolatileStorage<K, T extends Storable<K>> extends Storage<K, T> {
    private Map<K, T> data;

    protected VolatileStorage() {
        data = new HashMap<>();
    }

    protected abstract K generateKey();

    protected T get(K key) {
        return data.get(key);
    }

    protected K insert(T item) {
        K key = generateKey();
        if (key == null) return null;

        if (item.setKey(key)) {
            T storageItem = (T) item.makeCopy();
            data.put(key, item);
            return key;
        }

        return null;
    }


    protected void delete(K key) {
        data.remove(key);
    }


    protected boolean update(T updatedItem) {
        K key = updatedItem.getKey();
        if (key == null || !data.containsKey(key)) {
            return false;
        }
        data.put(key, updatedItem);
        return true;
    }


}