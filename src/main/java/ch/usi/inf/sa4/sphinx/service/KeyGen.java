package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Storable;

public abstract class KeyGen<T, X>{

    public abstract T generateKey(X item);
}
