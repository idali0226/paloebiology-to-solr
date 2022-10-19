package se.nrm.palebiology.data.process.logic;

import java.io.Serializable;
import java.util.List; 
import java.util.concurrent.TimeUnit; 
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;  
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.wildfly.swarm.Swarm;
import se.nrm.palebiology.data.process.config.InitialProperties;
import se.nrm.palebiology.data.process.logic.csv.CSVFileProcessor;
import se.nrm.palebiology.data.process.logic.json.JsonConverter;
import se.nrm.palebiology.data.process.logic.solr.SolrClient;

/**
 *
 * @author idali
 */ 
@Slf4j
public class DataProcessor implements Serializable {
  
  
  
  @Inject
  private InitialProperties propeties;
  @Inject
  private CSVFileProcessor fileProcessor;
  @Inject
  private JsonConverter jsonConverter;
  @Inject
  private SolrClient solr;

  public DataProcessor() {

  }

  public void run() {
    log.info("run"); 
     
    List<CSVRecord> records = fileProcessor.readCsvFile(propeties.getCsvFilePath());
    if (records != null) {
      log.info("records : {}", records.size());   
      JsonObject collectionJson = jsonConverter
              .getCollectionJson(propeties.getMappingFilePath(), propeties.getCollection());
      List<JsonArray> list = jsonConverter.convertCsvToJson(records, collectionJson);
      log.info("List size : {}", list.size()); 
      list.stream()
              .forEach(l -> { 
                int status = solr.postToSolr(propeties.getSolrPath(), l.toString().trim());
                log.info("status : {}", status);
              });  
    } 
    stopServer();
  }
  
    
  private void stopServer() {
    log.info("stopServer");
    try {
      TimeUnit.SECONDS.sleep(20);
      Thread.currentThread().interrupt();
      Swarm.stopMain(); 
    } catch (InterruptedException ex) {
      log.error(ex.getMessage());
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
  } 
 
}
  