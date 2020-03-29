package ch.usi.inf.sa4.sphinx.model;

public abstract class Effect<T> extends Storable<Integer> {
    public final int device;

    public Effect(int deviceId) {
        device = deviceId;
    }

    public abstract void execute(T effect);

    public boolean setId(Integer id) {
        return setKey(id);
    }


    public int getId() {
        return getKey();
    }
}
