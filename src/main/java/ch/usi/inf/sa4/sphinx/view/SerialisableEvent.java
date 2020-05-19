package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.model.triggers.EventType;

public class SerialisableEvent {
    private Integer source;
    private Integer type;
    private EventType eventType;
    private String value;
    private Boolean booleanValue;
    private Double doubleValue;


    public void setSource(Integer source) {
        this.source = source;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setValue(String value) {
        this.value = value;
        if (value.equals("true")) {
            booleanValue = true;
        } else if (value.equals("false")) {
            booleanValue = false;
        } else {
            try {
                doubleValue = Double.valueOf(value);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid double format");
            }
        }

    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }


    public Integer getSource() {
        return source;
    }

    public Integer getType() {
        return type;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getValue() {
        return value;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }
}
