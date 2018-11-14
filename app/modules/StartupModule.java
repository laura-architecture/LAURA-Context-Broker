package modules;

import models.context.Entity;
import models.context.IntrinsicContext;
import models.context.utils.Value;
import services.ContextBrokerService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StartupModule {

    @Inject
    StartupModule(ContextBrokerService contextBrokerService) {
        Entity mary = new Entity("Person").setDescriptor("Dr. Mary Sue");
        mary.getAttributes().put("age", new Value(35));
        contextBrokerService.registerEntity(mary);
        IntrinsicContext temperature = new IntrinsicContext(mary);
        temperature.setKind("Temperature");
        temperature.setDescriptor("Dr. Mary Sue's temperature");
        mary.getContexts().put(temperature.getKind(), temperature);
        contextBrokerService.registerIntrinsicContext(temperature);
    }

}
