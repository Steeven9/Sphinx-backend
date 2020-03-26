package ch.usi.inf.sa4.sphinx.model;

public abstract class Effect<T> {
    public final int device;
    private Integer id;

    public Effect(int deviceId) {
        device = deviceId;
    }

    public abstract void execute(T effect);

    public boolean setId(Integer id) {
        if (this.id != null) {
            this.id = id;
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }
}
