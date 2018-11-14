package models.context;

import java.util.HashMap;
import java.util.Map;

public class RelationalContext extends Context {

    private Map<String, Entity> parts = new HashMap<>();

    public RelationalContext() {
    }

    public RelationalContext(Long cid) {
        this.id = cid;
    }

    public Map<String, Entity> getParts() {
        return parts;
    }
}
