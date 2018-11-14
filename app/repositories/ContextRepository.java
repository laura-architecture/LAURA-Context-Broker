package repositories;

import models.context.Context;
import models.context.Entity;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class ContextRepository {


    private Map<Long, Context> contexts = new HashMap<>();


    public Context save(Context context) {
        context.setId(contexts.size() + 1);
        contexts.put(context.getId(), context);
        return context;
    }

    public Optional<Context> getById(Long id) {
        return Optional.ofNullable(contexts.get(id));
    }
}
