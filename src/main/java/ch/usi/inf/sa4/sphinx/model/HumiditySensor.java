package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;

/**
 * A humidity sensor measures humidity (in %) in a given room.
 */
public class HumiditySensor extends Sensor {

    /**
     * Constructor.
     * Creates a humidity sensor set to 32.0 percent.
     */
    public HumiditySensor(RoomService roomService, CouplingService couplingService) {
        super(32.0, roomService, couplingService);
    }

    /**
     * Constructor.
     * Given a HumiditySensor, creates a copy of it.
     * @param s The HumiditySensor to copy
     */
    public HumiditySensor(HumiditySensor s) {
        super(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HumiditySensor makeCopy() {
        return new HumiditySensor(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPhQuantity() {
        return "%";
    }
}