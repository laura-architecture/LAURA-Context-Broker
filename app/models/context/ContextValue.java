package models.context;

import models.Model;
import models.context.utils.Value;

import java.util.Map;

public class ContextValue extends Model {

    private Long timestamp;
    private Map<String, Value> entries;
    private Context owner;

    public ContextValue() {}

    public ContextValue (Context owner, Long timestamp) {
        this.owner = owner;
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Value> getEntries() {
        return entries;
    }

    public void setEntries(Map<String, Value> entries) {
        this.entries = entries;
    }

    public Context getOwner() {
        return owner;
    }

    public void setOwner(Context owner) {
        this.owner = owner;
    }
}
