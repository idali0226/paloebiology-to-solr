package se.nrm.palebiology.data.process.config;

import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author idali
 */
public class InitialPropertiesTest {
  
  private InitialProperties instance;
  private final String csvFilePath = "/Users/idali/temp/paleobiology/exportPAL.csv";
  private final String mappingfilePath = "mapping_files/solr_data_mapping.json";     
  private final String solrPath = "http://localhost:18983/solr/";
  private final String collection = "pb"; 
   
  public InitialPropertiesTest() { 
  }
  
  @Before
  public void setUp() {
    instance = new InitialProperties(); 
  }
  
  @After
  public void tearDown() {
    instance = null;
  }

  /**
   * Test of getCsvFilePath method, of class InitialProperties.
   */
  @Test
  public void testGetCsvFilePath() {
    System.out.println("getCsvFilePath");
    instance = new InitialProperties(solrPath, csvFilePath, mappingfilePath, collection); 
    String result = instance.getCsvFilePath();
    assertEquals(csvFilePath, result); 
  }
   
  @Test(expected = RuntimeException.class)
  public void testGetCsvFilePathException() {
    System.out.println("testGetCsvFilePathException");  
    instance.getCsvFilePath();
  }
 
  /**
   * Test of getSolrPath method, of class InitialProperties.
   */
  @Test
  public void testGetSolrPath() {
    System.out.println("getSolrPath");
    instance = new InitialProperties(solrPath, csvFilePath, mappingfilePath, collection); 
    String result = instance.getSolrPath();
    assertEquals(solrPath, result); 
  }
  
  @Test(expected = RuntimeException.class)
  public void testGetSolrPathException() {
    System.out.println("testGetSolrPathException");  
    instance.getSolrPath();
  }

  /**
   * Test of getMappingFilePath method, of class InitialProperties.
   */
  @Test
  public void testGetMappingFilePath() {
    System.out.println("getMappingFilePath");
    instance = new InitialProperties(solrPath, csvFilePath, mappingfilePath, collection); 
    String result = instance.getMappingFilePath();
    assertEquals(mappingfilePath, result); 
  }

  @Test(expected = RuntimeException.class)
  public void testGetMappingFilePatException() {
    System.out.println("testGetMappingFilePatException");  
    instance.getMappingFilePath();
  }
  
  /**
   * Test of getCollection method, of class InitialProperties.
   */
  @Test
  public void testGetCollection() {
    System.out.println("getCollection");
    instance = new InitialProperties(solrPath, csvFilePath, mappingfilePath, collection); 
    String result = instance.getCollection();
    assertEquals(collection, result); 
  }
  
  @Test(expected = RuntimeException.class)
  public void testGetCollectionException() {
    System.out.println("getCollectionException");  
    instance.getCollection();
  } 
}
