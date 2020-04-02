package ch.usi.inf.sa4.sphinx.model;


import javax.validation.constraints.NotNull;

/**
 * @param <K> The type of the key of the Storable item
 * @param <T>  The type of the Storable item
 */
public abstract class Storable<K, T> {
    private K key;
    private boolean canSetKey = true;

    protected Storable(){
    }

    /**
     * Sets the key of this object to desired value {@param key} if uninitialized
     * @param key the value to set
     * @return true if success else false
     */
    public boolean setKey( K key){
        if(canSetKey()){
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


    /**
     * Locks the key of this item
     */
    public void lockKey(){
        canSetKey = false;
    }


    /**
     * Asserts if the key of this item can be set
     * @return true if the key can be set else false
     */
    public boolean canSetKey(){
        return canSetKey;
    }
}
