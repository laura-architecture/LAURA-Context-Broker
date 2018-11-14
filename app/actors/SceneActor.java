package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import br.ufes.inf.lprm.scene.SceneApplication;
import javassist.ClassPool;
import javassist.LoaderClassPath;
import org.drools.compiler.kproject.models.KieModuleModelImpl;
import org.drools.core.ClockType;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.FactHandle;
import play.Logger;
import play.api.Environment;
import play.libs.Json;
import repositories.SituationRepository;
import scala.Option;
import scene.SituationBroadcaster;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SceneActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private final Set<ActorRef> subscribers;
    private final Environment env;

    private KieSession kSession;
    private CompletableFuture engine;

    private SituationRepository situationRepo;
    private KieSession newSession(Environment env, String sceneId) {
        // Getting KieServices
        KieServices kServices = KieServices.Factory.get();
        // Instantiating a KieFileSystem and KieModuleModel
        KieFileSystem kFileSystem = kServices.newKieFileSystem();
        KieModuleModel kieModuleModel = kServices.newKieModuleModel();

        ReleaseId releaseId = kServices.newReleaseId("br.ufes.inf.lprm.scene", sceneId, "0.1.0");
        kFileSystem.generateAndWritePomXML(releaseId);

        KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel(sceneId);
        kieBaseModel.setEventProcessingMode(EventProcessingOption.STREAM);
        //kieBaseModel.setEqualsBehavior(EqualityBehaviorOption.EQUALITY);
        kieBaseModel.addInclude("sceneKieBase");

        Option<URL> scenePath = env.resource("scene");

        if (scenePath.isEmpty()) throw new IllegalStateException("couldn't find scene's root dir");

        File userpath;
        try {
            userpath = new File(scenePath.get().toURI());
        } catch(URISyntaxException e) {
            userpath = new File(scenePath.get().getPath());
        }

        File[] files = userpath.listFiles(pathname -> pathname.getName().toLowerCase().endsWith(".drl"));

        if (files != null) {
            for (File file : files) {
                kFileSystem.write(
                        kServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL)
                );
            }
        }

        KieSessionModel kieSessionModel = kieBaseModel.newKieSessionModel(sceneId + ".kSession");
        kieSessionModel.setClockType(ClockTypeOption.get(ClockType.REALTIME_CLOCK.getId()))
                .setType(KieSessionModel.KieSessionType.STATEFUL);

        kFileSystem.writeKModuleXML(kieModuleModel.toXML());
        KieBuilder kbuilder = kServices.newKieBuilder(kFileSystem);
        ArrayList<Resource> dependencies = new ArrayList();
        try {
            Enumeration<URL> e = env.classLoader().getResources(KieModuleModelImpl.KMODULE_JAR_PATH);
            while ( e.hasMoreElements() ) {
                URL url = e.nextElement();
                String path;
                if (url.getPath().contains(".jar!")) {
                    path = url.getPath().replace("!/" + KieModuleModelImpl.KMODULE_JAR_PATH, "");
                    dependencies.add(kServices.getResources().newUrlResource(path));
                } else {
                    path = url.getPath().replace(KieModuleModelImpl.KMODULE_JAR_PATH, "");
                    dependencies.add(kServices.getResources().newFileSystemResource(path));
                }

            }
        } catch (IOException e1) {
            logger.error(e1.getMessage());
        }

        kbuilder.setDependencies(dependencies.toArray(new Resource[0]));
        kbuilder.buildAll();
        if (kbuilder.getResults().hasMessages()) {
            logger.error("Couldn't build knowledge module " + kbuilder.getResults());
            throw new IllegalArgumentException("Couldn't build knowledge module " + kbuilder.getResults());
        }

        KieModule kModule = kbuilder.getKieModule();
        KieContainer kContainer = kServices.newKieContainer(kModule.getReleaseId());

        KieSession session = kContainer.newKieSession(sceneId + ".kSession");
        session.addEventListener(new SituationBroadcaster(self(), subscribers, situationRepo, logger));
        return session;

    }

    @Override
    public void preStart() {

        Logger.debug("Starting scene actor");

        kSession = newSession(env, "scene-actor");

        ClassPool classPool = new ClassPool();
        classPool.appendSystemPath();
        //classPool.appendClassPath(new LoaderClassPath(Enhancer.class.getClassLoader()));
        classPool.appendClassPath(new LoaderClassPath(env.classLoader()));

        new SceneApplication(classPool, kSession, "scene-actor");

        kSession.setGlobal("logger", logger);

        engine = CompletableFuture.runAsync(kSession::fireUntilHalt)
              .exceptionally(
                err -> {
                    logger.error(err.toString());
                    return null;
                }
        );
    }

    @Override
    public void postStop() {
        logger.info("Shutting down SCENE engine...");
        engine.cancel(true);
        kSession.dispose();
    }

    @Inject
    public SceneActor(Environment env, SituationRepository situationRepo) {
        this.env = env;
        this.situationRepo = situationRepo;
        this.subscribers = new HashSet<>();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Protocols.Bulk.class, bulk -> {
                    kSession.submit(session -> bulk.operations.forEach(
                        op -> {
                            switch (op.type) {
                                case INSERT:
                                    if (session.getFactHandle(op.obj) == null) {
                                        logger.info("inserting object... {}", op.obj);
                                        session.insert(op.obj);
                                        logger.info("object inserted...");
                                    }
                                    break;
                                case UPDATE:
                                    FactHandle handle = session.getFactHandle(op.obj);
                                    logger.info("updating object... {}",  Json.toJson(op.obj));
                                    session.update(handle, op.obj);
                                    logger.info("object updated...");
                                    break;
                                case DELETE:
                                    session.delete(session.getFactHandle(op.obj));
                            }
                        }
                        )
                    );
                }).match(Protocols.Operation.class, (Protocols.Operation operation) -> {
                    Collection collection;
                    if (operation.obj instanceof Collection) {
                        collection = (Collection) operation.obj;
                    } else {
                        collection = new ArrayList();
                        collection.add(operation.obj);
                    }
                    switch (operation.type) {
                        case INSERT:
                            kSession.submit(session -> collection.forEach(item -> {
                                if (session.getFactHandle(item) == null) {
                                    logger.info("inserting object... {}", item);
                                    session.insert(item);
                                    logger.info("object inserted...");
                                }
                            } ));
                            break;
                        case UPDATE:
                            kSession.submit(session -> collection.forEach(item -> {
                                FactHandle handle = session.getFactHandle(item);
                                if (handle == null) {
                                    logger.info("fact handle for `{}` not found... inserting it", item);
                                    logger.info("inserting object... {}", Json.toJson(item));
                                    session.insert(item);
                                    logger.info("object inserted...");
                                } else {
                                    logger.info("updating object... {}",  Json.toJson(item));
                                    session.update(handle, item);
                                    logger.info("object updated...");
                                }
                            }));
                            break;
                        case DELETE:
                            kSession.submit(session -> collection.forEach(item -> session.delete(session.getFactHandle(item))));
                    }
                } )
                .match(Protocols.Subscribe.class, subscribe -> {
                    logger.info("adding a new subscriber for `{}`", self().path());
                    subscribers.add(sender());
                })
                .match(Protocols.Unsubscribe.class, unsubscribe -> {
                    logger.info("removing subscriber for `{}`", self().path());
                    subscribers.remove(sender());
                })
                .build();
    }

}
