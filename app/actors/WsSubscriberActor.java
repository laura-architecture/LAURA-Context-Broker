package actors;

import akka.actor.*;
import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class WsSubscriberActor extends AbstractActor {

    private ActorRef parent;
    private ActorRef out;
    private Scheduler scheduler;
    private Cancellable heartbeatTask;

    public static Props props(ActorRef parent, ActorRef out) {
        return Props.create(WsSubscriberActor.class,  parent, out);
    }

    public WsSubscriberActor(ActorRef parent, ActorRef out) {
        this.parent = parent;
        this.out = out;
        scheduler = getContext().getSystem().getScheduler();
    }

    @Override
    public void preStart() {
        parent.tell(new Protocols.Subscribe() , self());
        self().tell(new Protocols.Heartbeat(), self());
        heartbeatTask = scheduler.schedule(Duration.ZERO,
                Duration.ofSeconds(30), self(), new Protocols.Heartbeat(),
                getContext().getSystem().dispatcher(), null);
    }

    @Override
    public void postStop() {
        heartbeatTask.cancel();
        parent.tell(new Protocols.Unsubscribe() , self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JsonNode.class, message ->
                        out.tell(message, self())
                )
                .match(Protocols.Heartbeat.class, message -> {
                            ObjectNode node = Json.newObject();
                            node.put("heartbeat", true);
                            out.tell( node, self());
                        }
                )
                .build();
    }

}
