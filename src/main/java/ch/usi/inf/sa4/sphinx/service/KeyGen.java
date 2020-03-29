package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Storable;

public abstract class KeyGen<K, T>{

    public abstract K generateKey(T item);
}
