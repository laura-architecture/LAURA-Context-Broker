package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import models.context.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import repositories.ContextRepository;
import repositories.EntityRepository;
import scala.concurrent.ExecutionContextExecutor;
import services.ContextBrokerService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ContextBrokerController extends Controller {

    private final ActorSystem actorSystem;
    private final ExecutionContextExecutor exec;
    private final EntityRepository entityRepo;
    private final ContextRepository contextRepo;
    private final ContextBrokerService contextBrokerService;

    @Inject
    public ContextBrokerController(ActorSystem actorSystem,
                                   ExecutionContextExecutor exec,
                                   EntityRepository entityRepo,
                                   ContextRepository contextRepo,
                                   ContextBrokerService contextBrokerService) {
      this.actorSystem = actorSystem;
      this.exec = exec;
      this.entityRepo = entityRepo;
      this.contextRepo = contextRepo;
      this.contextBrokerService = contextBrokerService;
    }

    public Result getRelationById(Long relationId) {
        Optional<Context> context = contextRepo.getById(relationId);
        if (!context.isPresent()) return notFound(String.format("Context for id '%d' not found", relationId) );
        return ok(Json.toJson(context.get()));
    }

    public Result registerRelationValue(Long relationId) {
        Optional<Context> context = contextRepo.getById(relationId);
        if (!context.isPresent()) return notFound(String.format("Context for id '%d' not found", relationId) );
        JsonNode json = request().body().asJson();
        ContextValue newOne = Json.fromJson(json, ContextValue.class);
        contextBrokerService.registerContextValue(context.get(), newOne);
        return ok(Json.toJson(newOne));
    }

    public Result registerEntity() {
        JsonNode json = request().body().asJson();
        Entity newOne = Json.fromJson(json, Entity.class);
        return ok(Json.toJson(contextBrokerService.registerEntity(newOne)));
    }

    public Result getEntities() {
        return ok(Json.toJson(entityRepo.getAll()));
    }

    public Result registerIntrinsicContext(Long entityId) {
        Optional<Entity> bearerOpt = entityRepo.getById(entityId);
        if (!bearerOpt.isPresent()) return notFound("entity not found");
        JsonNode json = request().body().asJson();
        IntrinsicContext newOne = Json.fromJson(json, IntrinsicContext.class);
        newOne.setBearer(bearerOpt.get());
        bearerOpt.get().getContexts().put(newOne.getKind(), newOne);
        contextBrokerService.registerIntrinsicContext(newOne);
        return ok(Json.toJson(newOne));

    }

    public Result registerContextValue(long entityId, long contextId) {
        Optional<Context> context = contextRepo.getById(contextId);
        if (!context.isPresent()) return notFound(String.format("Context for id '%d' not found", contextId) );
        JsonNode json = request().body().asJson();
        ContextValue newOne = Json.fromJson(json, ContextValue.class);
        contextBrokerService.registerContextValue(context.get(), newOne);
        return ok(Json.toJson(newOne));
    }

    public Result getContext(Long entityId) {
        return Results.TODO;
    }

    public Result getContextValue() {
        return Results.TODO;
    }

    public Result registerAttribute(Long entityId) {
        return Results.TODO;
    }

    public Result getAttributes(Long entityId) {
        return Results.TODO;
    }

    public Result createRelationalContext() {

        JsonNode json = request().body().asJson();
        RelationalContext newOne = Json.fromJson(json, RelationalContext.class);
        contextBrokerService.registerRelationalContext(newOne, request().queryString());
        return ok(Json.toJson(newOne));
    }

    /**
     * An action that returns a plain text message after a delay
     * of 1 second.
     *
     * The configuration in the <code>routes</code> file means that this method
     * will be called when the application receives a <code>GET</code> request with
     * a path of <code>/message</code>.

    public CompletionStage<Result> message() {
        return getFutureMessage(1, TimeUnit.SECONDS).thenApplyAsync(Results::ok, exec);
    }

    private CompletionStage<String> getFutureMessage(long time, TimeUnit timeUnit) {
        CompletableFuture<String> future = new CompletableFuture<>();
        actorSystem.scheduler().scheduleOnce(
            Duration.create(time, timeUnit),
            () -> future.complete("Hi!"),
            exec
        );
        return future;
    }
     */
}
