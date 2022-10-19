package se.nrm.palebiology.data.process;

import java.util.concurrent.CompletableFuture; 
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j; 
import se.nrm.palebiology.data.process.logic.DataProcessor;
import se.nrm.palebiology.data.process.logic.TxtToCsv;

/**
 *
 * @author idali
 */
@Slf4j
@ApplicationScoped
public class Startup {

  @Inject
  private DataProcessor process;
  @Inject
  private TxtToCsv txtToCsv;        

  void init(@Observes @Initialized(ApplicationScoped.class) Object event) {
    log.info("StartupBean Application - INITIALIZATION");

    CompletableFuture.runAsync(() -> {
      process.run();
//      txtToCsv.convert();
      log.info("app processing....");
      
    }); 
  } 
}
