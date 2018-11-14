package io.json.mixins;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.json.serializers.RelationalContextSerializer;

@JsonSerialize(using = RelationalContextSerializer.class)
public abstract class RelationalContextMixin {

}
