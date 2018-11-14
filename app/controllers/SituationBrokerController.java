package controllers;

import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.situation.PersistentSituation;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.SituationRepository;
import scala.concurrent.ExecutionContext;
import scala.concurrent.ExecutionContextExecutor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class SituationBrokerController extends Controller {

    private final ActorSystem actorSystem;
    private final SituationRepository situationRepo;
    private final ExecutionContextExecutor exec;

    /**
     * @param actorSystem We need the {@link ActorSystem}'s
     * {@link Scheduler} to run code after a delay.
     * @param exec We need a Java {@link Executor} to apply the result
     * of the {@link CompletableFuture} and a Scala
     * {@link ExecutionContext} so we can use the Akka {@link Scheduler}.
     * An {@link ExecutionContextExecutor} implements both interfaces.
     */
    @Inject
    public SituationBrokerController(ActorSystem actorSystem, ExecutionContextExecutor exec, SituationRepository situationRepo) {
      this.actorSystem = actorSystem;
      this.exec = exec;
      this.situationRepo = situationRepo;
    }

    public Result uploadDRL() {
        return play.mvc.Results.TODO;
    }

    public Result getSituation(Long situationId) {
        return situationRepo
                .getById(situationId)
                .map(situation -> ok(Json.toJson(situation)))
                .orElse(notFound("situation not found"));
    }

    public Result getSituations() {
        ArrayNode situationsNode = Json.newArray();
        situationRepo.getAll().forEach(
                sit -> situationsNode.add(Json.toJson(sit))
        );
        return ok(situationsNode);
    }

}
