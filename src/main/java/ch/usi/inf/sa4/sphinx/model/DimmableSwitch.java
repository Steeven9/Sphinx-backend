package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;

/**
 * A DimmableSwitch is a switch that can set and store the intensity for given device.
 */
public class DimmableSwitch extends Dimmable {

    /**
     * Constructor.
     * Creates a dimmable switch.
     */
    public DimmableSwitch(RoomService roomService, CouplingService couplingService) {
        super(roomService, couplingService);
    }
}