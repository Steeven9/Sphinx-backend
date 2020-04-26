package ch.usi.inf.sa4.sphinx.model;

public class SecurityCamera extends Device {
    private boolean on;
    private String url;

    private  SecurityCamera(SecurityCamera other){
        this.on = other.isOn();
        this.url = other.getUrl();
    }

    protected String getUrl(){
        return this.url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.on + "";
    }

    @Override
    public Device makeCopy() {
        return new SecurityCamera(this);
    }
}
