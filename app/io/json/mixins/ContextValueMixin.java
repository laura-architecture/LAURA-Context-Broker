package io.json.mixins;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import models.context.Context;

public class ContextValueMixin {

    //@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    //@JsonIdentityReference(alwaysAsId=true)
    @JsonIgnore
    private Context owner;
}
