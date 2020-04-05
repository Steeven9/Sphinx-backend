package ch.usi.inf.sa4.sphinx.model;

public abstract class Effect<T> extends Storable<Integer, Effect<?>> {
    protected int deviceId;

    public Effect(Integer deviceId) {
        this.deviceId = deviceId;
    }

    protected Effect(Effect<T> effect){
        this.deviceId = effect.deviceId;
        setKey(effect.getKey());
    }

    public abstract void execute(T effect);
}
