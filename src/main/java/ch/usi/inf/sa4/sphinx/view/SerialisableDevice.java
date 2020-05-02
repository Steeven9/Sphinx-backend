package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class SerialisableDevice {

    public Integer id;
    public String icon;
    public String name;
    public String label;
    public int[] switched;
    public int[] switches;
    public Double slider;
    public Integer roomId;
    public String roomName;
    public Integer type;
    public String userName;
    public Boolean on;
    public String url;
    public double averageTemp;
    public int state;
    public int source;

    /**
     * Constructor.
     **/
    public SerialisableDevice() {
    }

}
