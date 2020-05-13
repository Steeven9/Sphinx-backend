//package ch.usi.inf.sa4.sphinx.model.conditions;
//
//import ch.usi.inf.sa4.sphinx.misc.EqualityOperator;
//import ch.usi.inf.sa4.sphinx.misc.StatusHolder;
//
//import javax.persistence.Entity;
//import javax.persistence.ManyToOne;
//
//@Entity
//public class EqualityCondition extends Condition<Object> {
//    @ManyToOne
//    private StatusHolder<?> device;
//    private EqualityOperator operator;
//
//    public EqualityCondition(StatusHolder<?> device,  Object target, EqualityOperator operator) {
//        super(target);
//        this.device = device;
//        this.operator = operator;
//    }
//
//    @Override
//    public boolean check() {
//        return operator.act(device.getStatus(), target);
//    }
//}