package ch.usi.inf.sa4.sphinx.model;

public abstract class Storable<K> {
    private K key;

    protected Storable(){
    }

    public boolean setKey(K key){
        if(key != null){
            this.key = key;
            return true;
        }
        return false;
    }

    public K getKey() {
        return key;
    }

    public abstract Storable<K> makeCopy();
}
