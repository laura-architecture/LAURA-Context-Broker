package io.json.serializers;

import br.ufes.inf.lprm.situation.model.Participation;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import models.situation.PersistentSituation;

import java.io.IOException;

public class PersistentSituationSerializer extends StdSerializer<PersistentSituation> {

    public PersistentSituationSerializer() {
        super(PersistentSituation.class);
    }

    @Override
    public void serialize(PersistentSituation value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        br.ufes.inf.lprm.situation.model.Situation innerSituation = value.getSituation();

        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("type", innerSituation.getType().getName());
        gen.writeBooleanField("active", innerSituation.isActive());
        gen.writeNumberField("started", innerSituation.getActivation().getTimestamp());
        if (!innerSituation.isActive()) {
            gen.writeNumberField("finished", innerSituation.getDeactivation().getTimestamp());
        }
        gen.writeObjectFieldStart("participations");

        for (Participation p : innerSituation.getParticipations()) {
            gen.writeFieldName(p.getPart().getLabel());
            gen.writeObject(p.getActor());
        }

        gen.writeEndObject();

    }

}
