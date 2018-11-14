package controllers;

import actors.WsSubscriberActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.stream.Materializer;
import com.google.inject.name.Named;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.WebSocket;
import scala.concurrent.ExecutionContext;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class SituationSubscriptionController extends Controller {

    private final ActorSystem actorSystem;
    private final ExecutionContextExecutor exec;
    private final ActorRef sceneActor;
    private final Materializer mat;

    @Inject
    public SituationSubscriptionController(ActorSystem actorSystem, Materializer mat, ExecutionContextExecutor exec, @Named("scene") ActorRef sceneActor) {
        this.actorSystem = actorSystem;
        this.exec = exec;
        this.sceneActor = sceneActor;
        this.mat = mat;
    }


    public WebSocket subscribe() {
        return WebSocket.Json.accept(
                request -> ActorFlow.actorRef(
                        out -> WsSubscriberActor.props(sceneActor, out),
                        actorSystem,
                        mat)
        );
    }

    public Result registerSubscription() {
        return Results.TODO;
    }

    public Result getSubscriptions() {
        return Results.TODO;
    }

    public Result getSubscription(String subscriptionId) {
        return Results.TODO;
    }


}
