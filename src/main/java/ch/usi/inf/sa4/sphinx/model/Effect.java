package ch.usi.inf.sa4.sphinx.model;

public abstract class Effect<T> {
    public final int device;
    private Integer id;

    /**
     * Constructor.
     * @param deviceId the id assigned to a device for a given effect
     **/
    public Effect(int deviceId) {
        device = deviceId;
    }

    /** Executes effect.
     * @param effect the effect to be executed
     **/
    public abstract void execute(T effect);

    /** Setter for the id.
     * @param id the id value we want to set to
     * @return true if value has been set (from null to the parameter int), false otherwise
     **/
    public boolean setId(Integer id) {
        if (this.id != null) {
            this.id = id;
            return true;
        }
        return false;
    }

    /** Getter for the id.
     * @return the id
     **/
    public int getId() {
        return id;
    }
}
