package ch.usi.inf.sa4.sphinx.model;

public  class DimmableSwitch extends Device implements SwitchInterface {
    private int state;
    public int getPower(){
        return state;
    }
    public String getLabel(){
     return state + "%";
    }


}