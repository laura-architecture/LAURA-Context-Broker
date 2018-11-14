package services;

import actors.Protocols;
import akka.actor.ActorRef;
import com.google.inject.name.Named;
import models.context.*;
import models.context.utils.Value;
import repositories.ContextRepository;
import repositories.EntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class SceneBrokerService implements ContextBrokerService {

    private final EntityRepository entityRepo;
    private final ContextRepository contextRepo;
    private final ActorRef sceneActor;

    @Inject
    SceneBrokerService(EntityRepository entityRepo, ContextRepository contextRepo, @Named("scene") ActorRef sceneActor) {
        this.entityRepo = entityRepo;
        this.contextRepo = contextRepo;
        this.sceneActor = sceneActor;
    }

    @Override
    public Entity registerEntity(Entity entity) {
        entityRepo.save(entity);
        sceneActor.tell(new Protocols.Operation(entity, Protocols.Operation.Type.INSERT), null );
        return entity;
    }

    @Override
    public Entity registerEntityAttribute(Entity entity, String key, Value value) {
        return null;
    }

    @Override
    public Context registerIntrinsicContext(IntrinsicContext context) {
        contextRepo.save(context);
        sceneActor.tell(new Protocols.Operation(context, Protocols.Operation.Type.INSERT), null );
        return context;
    }

    @Override
    public Context registerRelationalContext(RelationalContext context, Map<String, String[]> parts) {
        parts.forEach((key, value) -> {
            Optional<Entity> ent = entityRepo.getById(Long.valueOf(value[0]));
            if (ent.isPresent()) {
                context.getParts().put(key, ent.get());
            } else throw new RuntimeException(String.format("entity not found for %s and id %s", key, value[0]));
        });
        contextRepo.save(context);
        return context;
    }

    @Override
    public ContextValue registerContextValue(Context context, ContextValue value) {
        value.setOwner(context);
        context.setValue(value);
        List<Protocols.Operation> operations = new ArrayList<>();
        operations.add(new Protocols.Operation(value, Protocols.Operation.Type.INSERT));
        operations.add(new Protocols.Operation(value.getOwner(), Protocols.Operation.Type.UPDATE));
        sceneActor.tell(new Protocols.Bulk(operations), null );
        return value;
    }
}
