package ch.usi.inf.sa4.sphinx.view;

import java.util.List;

//        {
//        "type":1,
//        "name":"Light intensity",
//        "slider":5,
//        "devices":[{"id":15},{"id":19}]
//        }
public class SerialisableEffect {
    public Integer type;
    public String name;
    public Double slider;
    public List<Integer> device;
}
