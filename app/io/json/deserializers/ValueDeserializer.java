package io.json.deserializers;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import models.context.utils.Value;

import java.io.IOException;

public class ValueDeserializer extends StdDeserializer {

    public ValueDeserializer() {
        super(Value.class);
    }

    @Override
    public Value deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.isNumber()) {
            return new Value(node.asDouble());
        } else if (node.isBoolean()) {
            return new Value(node.asBoolean());
        } else return new Value(node.asText());
    }


}