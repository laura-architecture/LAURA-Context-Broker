package models.context.utils;

import models.Model;

public class Value extends Model {

    private String value;
    private ValueType type;

    public Value() {

    }

    public Value(double value) {
        this.type = ValueType.NUMBER;
        this.value = String.valueOf(value);
    }


    public Value(boolean value) {
        this.type = ValueType.BOOLEAN;
        this.value = String.valueOf(value);
    }

    public Value(String value) {
        this.type = ValueType.STRING;
        this.value = value;
    }

    public Value(ValueType type, String value) {
        this.type = type;
        this.value = value;
    }

    public boolean asBoolean() {
        return Boolean.parseBoolean(value);
    }

    public Double asNumber() {
        return Double.parseDouble(value);
    }

    public String asString() {
        return value;
    }

    public ValueType getType() {
        return type;
    }

}
