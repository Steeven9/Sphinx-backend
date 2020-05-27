package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;

import java.util.Optional;

public class SerialisableCondition {
    private ConditionType conditionType;
    private Integer type;
    private Integer source;
    private String value;


    public SerialisableCondition(ConditionType conditionType, Integer source, String value) {
        this.source = source;
        this.value = value;
        this.conditionType = conditionType;
        this.type = conditionType.toInt();
    }

    public ConditionType getConditionType() {
        return conditionType;
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

    public Optional<Double> getDoubleValue() {
        try {
            return Optional.of(Double.valueOf(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<Boolean> getBooleanValue() {
        if (value.equals("true")) {
            return Optional.of(true);
        }

        if (value.equals("false")) {
            return Optional.of(false);
        }

        return Optional.empty();
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
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
