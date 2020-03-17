package ch.usi.inf.sa4.sphinx.model;

public abstract class Effect<T> {
    public final int device;

    public Effect(int deviceId) {
        device = deviceId;
    }

    public abstract void execute(T data);
}
