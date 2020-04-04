package ch.usi.inf.sa4.sphinx.model;

public abstract class Effect<T> extends Storable<Integer, Effect<?>> {

    public Effect(Integer deviceId) {
        setKey(deviceId);
    }

    public abstract void execute(T effect);
}
