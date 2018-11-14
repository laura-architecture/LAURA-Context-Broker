package io.json.mixins;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.json.deserializers.SituationDeserializer;
import io.json.serializers.PersistentSituationSerializer;

@JsonSerialize(using = PersistentSituationSerializer.class)
@JsonDeserialize(using = SituationDeserializer.class)
public class SituationMixin {
}
