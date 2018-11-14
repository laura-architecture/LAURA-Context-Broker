package scene;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import br.ufes.inf.lprm.situation.model.Situation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Model;
import models.context.Entity;
import models.situation.PersistentSituation;
import models.situation.SituationEvent;
import models.situation.SituationEventType;
import org.kie.api.event.rule.DefaultRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import play.libs.Json;
import repositories.SituationRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

//import models.base.Entity;

public class SituationBroadcaster extends DefaultRuleRuntimeEventListener {

    ActorRef owner;
    Set<ActorRef> subscribers;
    SituationRepository situationRepo;
    LoggingAdapter logger;

    public SituationBroadcaster(ActorRef owner, Set<ActorRef> subscribers, SituationRepository situationRepo, LoggingAdapter logger) {
        this.logger = logger;
        this.owner = owner;
        this.subscribers = subscribers;
        this.situationRepo = situationRepo;
    }

    public String beautify(JsonNode jsonNode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(jsonNode.toString(), Object.class);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    public JsonNode toJson(SituationEvent event) {
        ObjectNode json = Json.newObject();

        json.put("type", event.getType().toString());

        json.put("timestamp", event.getTimestamp());

        ObjectNode situationNode = Json.newObject();
        json.set("situation", situationNode);

        Situation situation = event.getSituation().getSituation();

        situationNode.put("id", event.getSituation().getId());
        situationNode.put("type", situation.getType().getName());
        situationNode.put("active", situation.isActive());
        situationNode.put("started", situation.getActivation().getTimestamp());
        if (!situation.isActive()) {
            situationNode.put("finished", situation.getDeactivation().getTimestamp());
        }

        ArrayNode participationsNode = situationNode.putArray("participations");

        situation.getParticipations().forEach(
                participation -> {
                    ObjectNode participationNode = Json.newObject();
                    if (participation.getActor() instanceof Model) {
                        participationNode.put("id", ((Model) participation.getActor()).getId());
                    }
                    if (participation.getActor() instanceof Model) {
                        participationNode.put("kind", ((Entity) participation.getActor()).getKind());
                    }
                    else participationNode.put("kind", participation.getActor().getClass().getCanonicalName());

                    participationNode.put("as", participation.getPart().getLabel());
                    participationsNode.add(participationNode);
                }
        );

        return json;
    }

    @Override
    public void objectInserted(ObjectInsertedEvent event) {
        try {
            Object in = event.getObject();
            if (in instanceof Situation) {

                PersistentSituation situation = situationRepo.save((Situation) in);
                SituationEvent activation = new SituationEvent(SituationEventType.ACTIVATION, ((Situation) in).getActivation().getTimestamp(), situation);

                logger.info(beautify(Json.toJson(activation)));
                subscribers.forEach(
                        subscriber -> subscriber.tell(Json.toJson(activation), owner)
                );
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent event) {
        try {
            Object in = event.getObject();
            if (in instanceof Situation) {
                Optional<PersistentSituation> situationOpt = situationRepo.getByOngoingId(((Situation) in).getUID());
                situationRepo.removeOngoing(((Situation) in).getUID());
                if (!situationOpt.isPresent()) throw new RuntimeException("situation not found");
                SituationEvent deactivation = new SituationEvent(SituationEventType.DEACTIVATION, ((Situation) in).getDeactivation().getTimestamp(), situationOpt.get());
                logger.info( beautify(Json.toJson(deactivation)));
                subscribers.forEach(
                        subscriber -> subscriber.tell(Json.toJson(deactivation), owner)
                );
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
