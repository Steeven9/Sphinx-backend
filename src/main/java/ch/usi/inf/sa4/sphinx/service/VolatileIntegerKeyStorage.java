package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Storable;

public abstract class VolatileIntegerKeyStorage<T extends Storable<Integer>> extends VolatileStorage<Integer, T> {
    private Integer id;


    protected VolatileIntegerKeyStorage() {
        this.id = 1;
    }


    @Override
    protected Integer generateKey() {
        return id++;
    }
}
