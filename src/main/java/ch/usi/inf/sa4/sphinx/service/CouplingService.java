package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.model.Coupling.BadCouplingException;
import ch.usi.inf.sa4.sphinx.model.Coupling.Coupling;
import ch.usi.inf.sa4.sphinx.model.Coupling.CouplingFactory;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Observer;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Coupling service..
 * It has methods to interact with Coupling entities.
 * In general it implements a layer of abstraction over the storage.
 *
 * @see Coupling
 */
@Service
@Transactional
public class CouplingService {

    @Autowired
    private CouplingStorage couplingStorage;
    @Autowired
    private DeviceStorage deviceStorage;
    @Autowired
    private DeviceService deviceService;


    /**
     * Getter for observers in storage.
     *
     * @param id the id of the coupling to get
     * @return an instance of Coupling
     **/
    public Optional<Coupling> get(final Integer id) {
        return couplingStorage.findById(id);
    }

    /**
     * Delete coupling in storage.
     *
     * @param id the id of the coupling to be deleted
     **/
    public void delete(final Integer id) {
        couplingStorage.deleteById(id);
    }


    /**
     * //     * @param id1 first id of the device observers that needs to be deleted
     * //     * @param id2 second id of the device observers that needs to be deleted
     * //
     */
    public void removeByDevicesIds(final Integer id1, final Integer id2) {
        try {
            Coupling coupling = couplingStorage.findByDeviceIdAndDevice2Id(id1, id2).orElseThrow(() -> new NotFoundException(""));
            Device device = coupling.getDevice();
            device.removeObserver(coupling);
            deviceService.update(device);
            couplingStorage.deleteByDeviceIdAndDevice2Id(id1, id2);
        } catch (NotFoundException e){
            Coupling coupling = couplingStorage.findByDeviceIdAndDevice2Id(id2, id1).orElseThrow(() -> new NotFoundException(""));
            Device device = coupling.getDevice();
            device.removeObserver(coupling);
            deviceService.update(device);
            couplingStorage.deleteByDeviceIdAndDevice2Id(id2, id1);
        }
    }



    public Coupling createCoupling(final int deviceId1, final int deviceId2) {
        Device device1 = deviceStorage.findById(deviceId1).orElseThrow(() -> new NotFoundException("device1 nf"));
        Device device2 = deviceStorage.findById(deviceId2).orElseThrow(() -> new NotFoundException("device2 nf"));
        return createCoupling(device1, device2);
    }

    public Coupling createCoupling(final @NonNull Device device1, final @NonNull Device device2) throws BadCouplingException {
        Coupling coupling = CouplingFactory.make(device1, device2).orElseThrow(BadCouplingException::new);
        coupling = couplingStorage.save(coupling);
        device1.addObserver(coupling);
        deviceStorage.save(device1);
        return coupling;
    }
}
