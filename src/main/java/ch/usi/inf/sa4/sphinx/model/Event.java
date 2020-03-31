package ch.usi.inf.sa4.sphinx.model;

public abstract class Event<T> {
    public final int device;
    private Integer id;

    /** Getter for the device.
     * @return the device
     **/
    public int getDevice() {
        return device;
    }

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

    /** Constructor.
     * @param deviceId  the id of the device for the Event
     **/
    public Event(int deviceId) {
        device = deviceId;
    }

    /** Getter function.
     * @return event
     **/
    public abstract T get();

    /** Getter for the id.
     * @return the id
     **/
    public Integer getId() {
        return id;
    }
}
