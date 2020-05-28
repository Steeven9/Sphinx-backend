package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.triggers.ConditionType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.Optional;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SerialisableCondition {
    @JsonIgnore
    private ConditionType type;
    private Integer conditionType;
    @JsonProperty("sourceId")
    @SerializedName("sourceId")
    private Integer source;
    @SerializedName("effectValue")
    @JsonProperty("effectValue")
    private String value;


    public SerialisableCondition(){}

    public SerialisableCondition(ConditionType conditionType, Integer source, String value) {
        this.source = source;
        this.value = value;
        this.type = conditionType;
        this.conditionType = conditionType.toInt();
    }

    public ConditionType getType() {
        if(type != null){
            return type;
        }
        return conditionType !=null? ConditionType.intToType(conditionType):null;

    }

    public Integer getConditionType() {
        return conditionType;
    }

    public Integer getSource() {
        return source;
    }

    public String getValue() {
        return value;
    }

    public Object getTarget() {
        if ("true".equals(value)) {
            return Boolean.TRUE;
        }

        if ("false".equals(value)) {
            return Boolean.FALSE;
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
        if ("true".equals(value)) {
            return Optional.of(Boolean.TRUE);
        }

        if ("false".equals(value)) {
            return Optional.of(Boolean.FALSE);
        }

        return Optional.empty();
    }

    public void setType(ConditionType type) {
        this.type = type;
        this.conditionType = type.toInt();
    }

    public void setConditionType(Integer conditionType) {
        this.conditionType = conditionType;
        this.type = ConditionType.intToType(conditionType);

    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
