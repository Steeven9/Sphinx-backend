package ch.usi.inf.sa4.sphinx.model;

public  class Switch extends Device implements SwitchInterface {
    private int state;

    /* Returns the power percentage for a given switch.
     * @return the value (percentage) of a given switch
     */
    public int getPower(){
        return state;
    }
    /* Computes whether the power is 'on' or 'off'.
     * @return a String stating whether switch is 'on' or 'off'
     */
    public String getLabel(){
        return state == 0 ? "OFF": "ON";
    }

}