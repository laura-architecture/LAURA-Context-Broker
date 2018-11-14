package io.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.AbstractModule;
import io.json.mixins.*;
import models.context.*;
import models.context.utils.Value;
import models.situation.PersistentSituation;
import play.libs.Json;

public class JsonMapperModule extends AbstractModule {

    private static class JsonMapper {
        JsonMapper() {
            ObjectMapper mapper = Json.newDefaultMapper();
            SimpleModule module = new SimpleModule("DataSerializer", new Version(1, 0, 0, null, null, null));
            module.setMixInAnnotation(Entity.class, EntityMixin.class);
            module.setMixInAnnotation(Value.class, ValueMixin.class);
            module.setMixInAnnotation(Context.class, ContextMixin.class);
            module.setMixInAnnotation(IntrinsicContext.class, IntrinsicContextMixin.class);
            module.setMixInAnnotation(RelationalContext.class, RelationalContextMixin.class);
            module.setMixInAnnotation(ContextValue.class, ContextValueMixin.class);
            module.setMixInAnnotation(PersistentSituation.class, SituationMixin.class);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.registerModule(module);
            Json.setObjectMapper(mapper);
        }
    }

    @Override
    protected void configure() {
        bind(JsonMapper.class).asEagerSingleton();
    }

}

