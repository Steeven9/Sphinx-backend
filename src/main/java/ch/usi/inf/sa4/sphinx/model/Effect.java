package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

public abstract class Effect<T> extends Storable<Integer, Effect<?> > {
    public final int device;

    public Effect(int deviceId) {
        device = deviceId;
    }

    public abstract void execute(T effect);


    public int getId() {
        return getKey();
    }
}
