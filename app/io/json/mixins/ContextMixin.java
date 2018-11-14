package io.json.mixins;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import models.context.IntrinsicContext;
import models.context.RelationalContext;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible=true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IntrinsicContext.class, name = "intrinsic"),
        @JsonSubTypes.Type(value = RelationalContext.class, name = "relational")
})
public class ContextMixin {
}
