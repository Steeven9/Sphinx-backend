package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;

public class SerialisableCondition {
    private ConditionType eventType;
    private Integer type;
    private Integer source;
    private String value;
    private Double doubleValue;
    private Boolean booleanValue;


    public SerialisableCondition(ConditionType eventType, Integer source, String value) {
        this.eventType = eventType;
        this.source = source;
        this.value = value;
    }

    public ConditionType getEventType() {
        return eventType;
    }

    public Integer getType() {
        return type;
    }

    public Integer getSource() {
        return source;
    }

    public String getValue() {
        return value;
    }

    public Object getTarget() {
        if (value.equals("true")) {
            return true;
        }

        if (value.equals("false")) {
            return false;
        }

        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            return value; //the beauty of javasript types now ported in Java
        }

    }

    public void setEventType(ConditionType eventType) {
        this.eventType = eventType;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
