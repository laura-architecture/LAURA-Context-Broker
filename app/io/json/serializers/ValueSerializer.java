package io.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import models.context.utils.Value;
import models.context.utils.ValueType;

import java.io.IOException;

public class ValueSerializer extends StdSerializer<Value> {

    public ValueSerializer() {
        super(Value.class);
    }

    @Override
    public void serialize(Value value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value.getType().equals(ValueType.NUMBER)) {
            gen.writeNumber(value.asNumber());
        } else if (value.getType().equals(ValueType.BOOLEAN)) {
            gen.writeBoolean(value.asBoolean());
        } else gen.writeString(value.asString());
    }
}