//package ch.usi.inf.sa4.sphinx.model.events;
//
//import ch.usi.inf.sa4.sphinx.model.*;
//import ch.usi.inf.sa4.sphinx.model.effects.DeviceSetOnEffect;
//import ch.usi.inf.sa4.sphinx.model.effects.DimmableLightStateInc;
//import ch.usi.inf.sa4.sphinx.model.effects.DimmableLightStateSet;
//import ch.usi.inf.sa4.sphinx.model.effects.Effect;
//
//public class EventFactory {
//    public Event make(Device d1, Device d2){
//        return make(d1, d2, true);
//    }
//
//    private Event make(Device d1, Device d2, boolean firstIteration){
//        try {
//            if(d1 instanceof Switch){
//                return new SwitchChangedEvent((Switch) d1, new DeviceSetOnEffect(d2));
//            }
//
//            if(d1 instanceof StatelessDimmableSwitch){
//                Effect<Double> effect= new DimmableLightStateInc((DimmableLight) d2);
//                return new StatelessDimmSwitchChangedEvent((StatelessDimmableSwitch) d1, effect, 0.1);
//            }
//
//            if(d1 instanceof DimmableSwitch){
//                 Effect<Double> effect = new  DimmableLightStateSet((DimmableLight) d2);
//                return new DimmSwitchChangedEvent((DimmableSwitch) d1, effect );
//            }
//
//            throw new IllegalArgumentException("No compatible device coupling found");
//
//        } catch (ClassCastException e){
//            if(firstIteration) return make(d2, d1, false);
//            throw new IllegalArgumentException("No compatible device coupling found");
//        }
//    }
//
//}
