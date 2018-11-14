package services;

import models.context.*;
import models.context.utils.Value;

import java.util.HashMap;
import java.util.Map;

public interface ContextBrokerService {
    public Entity registerEntity(Entity entity);
    public Entity registerEntityAttribute(Entity entity, String key, Value value);
    public Context registerIntrinsicContext(IntrinsicContext context);
    public Context registerRelationalContext(RelationalContext context, Map<String, String[]> parts);
    public ContextValue registerContextValue(Context context, ContextValue value);
}
