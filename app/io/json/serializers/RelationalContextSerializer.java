package io.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import models.context.RelationalContext;

import java.io.IOException;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class RelationalContextSerializer extends StdSerializer<RelationalContext> {

    public RelationalContextSerializer() {
        super(RelationalContext.class);
    }

    public void serializeWithType(RelationalContext value, JsonGenerator gen, SerializerProvider serializers,
                                  TypeSerializer typeSer)
            throws IOException
    {
        serialize(value, gen, serializers);
    }

    @Override
    public void serialize(RelationalContext value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        if (value.getId() != null) gen.writeNumberField("id", value.getId());
        if (value.getType() != null) gen.writeStringField("type", value.getType().name());
        if (value.getKind() != null) gen.writeStringField("kind", value.getKind());
        if (value.getDescriptor() != null) gen.writeStringField("descriptor", value.getDescriptor());

        Map<String, Long> parts = value.getParts()
                                        .entrySet().stream()
                                        .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue().getId()));

        gen.writeObjectField("parts", parts);
        gen.writeEndObject();

    }

}
