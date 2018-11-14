package models.context;

import models.Model;
import models.context.utils.Value;

import java.util.HashMap;
import java.util.Map;

public class Entity extends Model {
    private String kind;
    private String descriptor;
    private Map<String, Value> attributes = new HashMap<>();
    private Map<String, Context> contexts = new HashMap<>();

    public Entity() {
    }

    public Entity(String kind) {
        this.kind = kind;
    }

    public Entity(Long eid, String kind) {
        this(kind);
        this.id = eid;
    }

    public String getKind() {
        return kind;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public Entity setDescriptor(String descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    public Map<String, Value> getAttributes() {
        return attributes;
    }

    public Map<String, Context> getContexts() {
        return contexts;
    }
}
