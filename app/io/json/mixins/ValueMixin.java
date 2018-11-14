package io.json.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.json.deserializers.ValueDeserializer;
import io.json.serializers.ValueSerializer;

@JsonDeserialize(using = ValueDeserializer.class)
@JsonSerialize(using = ValueSerializer.class)
public abstract class ValueMixin { }
