package ch.usi.inf.sa4.sphinx.model;


import javax.validation.constraints.NotNull;

/**
 * @param <K> The type of the key of the Storable item
 * @param <T>  The type of the Storable item
 */
public abstract class Storable<K, T> {
    private K key;

    protected Storable(){
    }

    /**
     * Sets the key of this object to desired value {@param key} if uninitialized
     * @param key the value to set
     * @return true if success else false
     */
    public boolean setKey(K key){
        if(key != null){
            this.key = key;
            return true;
        }
        return false;
    }

    /**
     * @return the key for this object
     */
    public K getKey() {
        return key;
    }

    /**
     * Must create a deep copy of this object
     * @return the copy of this
     */
    public abstract @NotNull T makeCopy();
}
