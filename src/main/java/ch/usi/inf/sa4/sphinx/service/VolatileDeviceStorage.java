package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Device;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;


@Repository("volatileDeviceStorage")
public class VolatileDeviceStorage extends VolatileIntegerKeyStorage<Device>{
    private VolatileDeviceStorage(){}
}
