package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;

public class SerialisableEvent {
    private Integer source;
    private Integer type;
    private ConditionType eventType;
    private String value;
    private Boolean booleanValue;
    private Double doubleValue;


    public void setSource(Integer source) {
        this.source = source;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setEventType(ConditionType eventType) {
        this.eventType = eventType;
    }

    public void setValue(String value) {
        this.value = value;
        if ("true".equals(value)) {
            booleanValue = Boolean.TRUE;
        } else if ("false".equals(value)) {
            booleanValue = Boolean.FALSE;
        } else {
            try {
                doubleValue = Double.valueOf(value);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid double format", e);
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

    public ConditionType getEventType() {
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
