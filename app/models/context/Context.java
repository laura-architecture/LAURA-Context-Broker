package models.context;

import models.Model;

public abstract class Context extends Model {

    private ContextType type;
    private String descriptor;
    private String kind;
    private ContextValue value;

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public ContextValue getValue() {
        return value;
    }

    public void setValue(ContextValue value) {
        this.value = value;
    }


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public ContextType getType() {
        return type;
    }

    public void setType(ContextType type) {
        this.type = type;
    }
}
