package ch.usi.inf.sa4.sphinx.model;

public  class Switch extends Device implements SwitchInterface {
    private int state;
    public int getPower(){
        return state;
    }

    public String getLabel(){
        return state == 0 ? "FALSE": "ON";
    }

}