package io.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import models.situation.PersistentSituation;

import java.io.IOException;

public class SituationDeserializer extends StdDeserializer {

    public SituationDeserializer() {
        super(PersistentSituation.class);
    }

    @Override
    public PersistentSituation deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        PersistentSituation sit = new PersistentSituation();
        sit.setId(node.get("id").asLong());
        return sit;

    }

}
