//package ch.usi.inf.sa4.sphinx.model;
//
//import ch.usi.inf.sa4.sphinx.model.effects.Effect;
//
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//
////@Entity
//public class RunAutomation extends Effect<Object> {
//    @ManyToOne
//    @JoinColumn(referencedColumnName = "id")
//    private Automation automation;
//
//    /**
//     * Constructor.
//     *
//     * @param automation the Automation to run
//     **/
//    public RunAutomation(final Automation automation) {
//        super(null);
//        this.automation = automation;
//    }
//
//
//
//    /**
//     * Runs the Automation.
//     *
//     * @param effect: new value of the state
//     **/
//    public void execute(final Object effect) {
//        automation.run();
//    }
//}
