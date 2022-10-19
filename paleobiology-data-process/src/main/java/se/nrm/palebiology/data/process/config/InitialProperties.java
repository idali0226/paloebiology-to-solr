package se.nrm.palebiology.data.process.config;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

/**
 *
 * @author idali
 */
@ApplicationScoped
@Slf4j
public class InitialProperties implements Serializable {

  private final static String CONFIG_INITIALLISING_ERROR = "Property not initialized";
 
  private String csvFilePath; 
  private String mappingfilePath;
  private String solrPath;
  private String collection;

  public InitialProperties() {
  }

  @Inject
  public InitialProperties(@ConfigurationValue("swarm.solr.path") String solrPath,
          @ConfigurationValue("swarm.csv.file.path") String csvFilePath,
          @ConfigurationValue("swarm.mapping.file") String mappingfilePath,
          @ConfigurationValue("swarm.collection") String collection) {
    this.solrPath = solrPath;
    this.csvFilePath = csvFilePath; 
    this.mappingfilePath = mappingfilePath;
    this.collection = collection;
  }
  
  public String getCsvFilePath() {
    if (csvFilePath == null) {
      throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
    }
    return csvFilePath;
  }

  public String getSolrPath() {
    if (solrPath == null) {
      throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
    }
    return solrPath;
  }
  
  public String getMappingFilePath() {
    if (mappingfilePath == null) {
      throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
    }
    return mappingfilePath;
  }
  
  public String getCollection() {
    if (collection == null) {
      throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
    }
    return collection;
  }
}