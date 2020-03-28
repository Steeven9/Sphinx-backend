package ch.usi.inf.sa4.sphinx.service;


import ch.usi.inf.sa4.sphinx.model.Storable;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;

import java.util.HashMap;
import java.util.Map;

public abstract class VolatileStorage<K, T> extends Storage<K, T> {
    private Map<K, Storable<K, T> > data;
    private KeyGen<K, Storable<K, T>> keyGen;


    protected VolatileStorage(KeyGen<K, Storable<K, T> > keygen) {
        data = new HashMap<>();
        this.keyGen = keygen;
    }

     protected Storable get(K key) {
        return data.get(key);
    }

     protected K insert(Storable<K, T> item) {
        K key = keyGen.generateKey(item);
        if(key == null) return null;

        if (item.setKey(key)) {
            data.put(key, item);
            return key;
        }
        return null;
    }

    protected void delete(K key){
        data.remove(key);
    }

    protected boolean update(Storable<K, T> updatedItem){
        K key = updatedItem.getKey();
        if(key == null || !data.containsKey(key)){
            return false;
        }
        data.put(key, updatedItem);
        return true;
    }


}