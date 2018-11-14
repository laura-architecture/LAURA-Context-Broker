package di;

import actors.SceneActor;
        import com.google.inject.AbstractModule;
import modules.StartupModule;
import play.libs.akka.AkkaGuiceSupport;
import services.ContextBrokerService;
import services.SceneBrokerService;

public class DIModule extends AbstractModule implements AkkaGuiceSupport {
    @Override
    protected void configure() {
        bindActor(SceneActor.class, "scene");
        bind(ContextBrokerService.class)
                .to(SceneBrokerService.class);
        bind(StartupModule.class).asEagerSingleton();
    }
}
