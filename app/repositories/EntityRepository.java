package repositories;

import models.context.Context;
import models.context.Entity;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class EntityRepository {

    private Map<Long, Entity> entities = new HashMap<>();

    public Entity save(Entity entity) {
        entity.setId(entities.size() + 1);
        entities.put(entity.getId(), entity);
        return entity;
    }

    public Collection<Entity> getAll() {
        return entities.values();
    }

    public Optional<Entity> getById(Long id) {
        return Optional.ofNullable(entities.get(id));
    }

}
