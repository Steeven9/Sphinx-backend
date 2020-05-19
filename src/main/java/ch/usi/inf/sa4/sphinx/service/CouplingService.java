package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.model.Coupling.BadCouplingException;
import ch.usi.inf.sa4.sphinx.model.Coupling.Coupling;
import ch.usi.inf.sa4.sphinx.model.Coupling.CouplingFactory;
import ch.usi.inf.sa4.sphinx.model.Device;
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
public class CouplingService {

    @Autowired
    private CouplingStorage couplingStorage;
    @Autowired
    private DeviceStorage deviceStorage;


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
        couplingStorage.deleteByDevice1IdAndDevice2Id(id1, id2);
    }


    public Coupling createCoupling(final int deviceId1, final int deviceId2) {
        Device device1 = deviceStorage.findById(deviceId1).orElseThrow(() -> new NotFoundException("device1 nf"));
        Device device2 = deviceStorage.findById(deviceId2).orElseThrow(() -> new NotFoundException("device2 nf"));
        return createCoupling(device1, device2);
    }

//    @Transactional
    public Coupling createCoupling(final @NonNull Device device1, final @NonNull Device device2) throws BadCouplingException {
        Coupling coupling = CouplingFactory.make(device1, device2).orElseThrow(BadCouplingException::new);
        coupling = couplingStorage.save(coupling);
        device1.addObserver(coupling);
        deviceStorage.save(device1);
        return coupling;
    }


//    /**
//     * @param id id of the Coupling that owns the effects
//     * @return the effects belonging to the specified coupling
//     */
//    public List<Effect> getEffects(@NotNull final Integer id) {
//        return couplingStorage.findById(id).map(Coupling::getEffects).orElse(new ArrayList<>());
//    }

//    /**
//     * @param id1 first id of the device observers that needs to be deleted
//     * @param id2 second id of the device observers that needs to be deleted
//     */
//    public void removeByDevicesIds(final Integer id1, final Integer id2){
//        final List<Coupling> couplings = couplingStorage.findAll();
//        for (final Coupling coupling : couplings) {
//            final List<Effect> effects = coupling.getEffects();
//
//            for (final Effect effect : effects) {
//                boolean boolId1 = false;
//                boolean boolId2 = false;
//
//                if(effect.getDeviceId().equals(id1)){
//                    boolId1 = true;
//                } else if(effect.getDeviceId().equals(id2)){
//                    boolId2 = true;
//                }
//                if(boolId1 && coupling.getEvent().getDeviceId() == id2){ //if effect.deviceId is id1 then event.deviceId should be id2
//                    effects.remove(effect);
//                }else if(boolId2 && coupling.getEvent().getDeviceId() == id1){//if effect.deviceId is id2 then event.deviceId should be id1
//                    effects.remove(effect);
//                }
//            }
//
//            if(effects.size() > 1){
//                couplingStorage.save(coupling);
//            }else{
//                //deleting the coupling
//                couplingStorage.delete(coupling);
//            }
//        }
//    }

//    /**
//     * Add coupling to storage.
//     *
//     * @param event  the event in the coupling to be added
//     * @param effects the effect in the coupling to be added
//     * @return the id of the new coupling
//     *
//     * @param <T> parametrized type of the Event and Effect*/
//    public <T> Integer addCoupling(final Event<T> event, final List<? extends Effect<T>> effects) {
//            final Coupling newCoupling = new Coupling(event, effects);
//            return couplingStorage.save(newCoupling).getId();
//    }

//
//    /**
//     * Works like {@link CouplingService#addCoupling(Event, List)} but with a single effect.
//     * @param event  the event in the coupling to be added
//     * @param effect the effect in the coupling to be added
//     * @return the id of the new coupling
//     *
//     * @param <T> parametrized type of the Event and Effect
//     * */
//    public <T> Integer addCoupling(final Event<T> event, final Effect<T> effect) {
//        return addCoupling(event, List.of(effect));
//    }


//    /**
//     * Add effect to coupling.
//     *
//     * @param id     the id of the coupling where we want to add an effect
//     * @param effect the id of the effect we want to add
//     * @return true if successfully updated, false otherwise
//     **/
//    public boolean addEffect(final Integer id, @NotNull final Effect effect) {
//        return couplingStorage.findById(id).map(coupling -> {
//                    try {
//                        coupling.addEffect(effect);
//                        couplingStorage.save(coupling);
//                        return true;
//                    } catch (IllegalArgumentException e) {
//                        return false;
//                    }
//                }
//        ).orElse(false);
//    }
}
