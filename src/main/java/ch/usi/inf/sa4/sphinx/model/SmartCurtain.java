package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;

/**
 * A SmartCurtain is a type of device that can be shadowed.
 */
public class SmartCurtain extends Dimmable {

    /**
     * Constructor.
     * Creates a smart curtain that can be shadowed.
     */
    public SmartCurtain(RoomService roomService, CouplingService couplingService) {
        super(roomService, couplingService);
    }
}
