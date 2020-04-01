package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Room;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository("volatileRoomStorage")
public final class VolatileRoomStorage extends VolatileIntegerKeyStorage<Room> {
    private VolatileRoomStorage(){}
}

