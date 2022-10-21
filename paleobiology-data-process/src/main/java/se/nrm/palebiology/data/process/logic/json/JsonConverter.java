package se.nrm.palebiology.data.process.logic.json;
  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;  
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder; 
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import se.nrm.palebiology.data.process.logic.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class JsonConverter implements Serializable {
  
  private final String collectionNameKey = "collectionName";  
  private final String catalogueIdKey = "Catalogue ID"; 
  private final String idPrefixKey = "idPrefix";
  private final String collectionIdKey = "collectionId";
  private final String classificationKey = "classification";
  private final String higherTxKey = "higherTx";
  private final String collDateKey = "collDate";
  private final String submippingKey = "submapping";
  private final String collYearKey = "collYear";
  private final String collMonthKey = "collMonth";
  private final String collDayKey = "collDay"; 
  private final String coordinateKey = "coordinate"; 
  private final String startDateKey = "startDate";
  private final String catalogedDateKey = "catalogedDate";
  private final String catalogedMonthKey = "catalogedMonth";
  private final String catalogedMonthNameKey = "catalogedMonthString";
  private final String catalogedYearKey = "catalogedYear";
  private final String txFullNameKey = "txFullName";
  private final String synonymKey = "synonym";
  private final String synonymAuthorKey = "synonymAuthor"; 
  private final String typeStatusKey = "typeStatus";
  private final String isTypeKey = "isType";
  private final String countryKey = "country";
  private final String inSwedenKey = "inSweden";
  
  private final String speciesName = "Species name";
  private final String author = "Author";
  
  private final String mapKey = "map";
  private final String mappingKey = "mapping";
  private final String latitudeKey = "latitude";
  private final String longtitudeKey = "longitude";
  private final String speciesKey = "species";
  private final String genusKey = "genus";
  private final String familyKey = "family";
  private final String sweden = "Sweden";
  
  
//  private final String regex = "\\D"; 
//  private final String specialChartRegex = "^[a-zåöäA-Z0-9]+$";
//  private final String emptyString = "";
  private final String emptySpace = " ";
  
  private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
  private final String slash = "/";
  private final String n = "N";
  private final String e = "E"; 
  private final String dash = "-";
  private final String strOne = "01";
  
  private final int batchSize = 2000;
 
  private JsonObjectBuilder builder;
  private JsonArrayBuilder arrayBuilder; 
//  private JsonObjectBuilder synomyBuilder;
  private JsonArrayBuilder synomysArrayBuilder;
//  private JsonObjectBuilder synomyAuthorBuilder;
  private JsonArrayBuilder synomyAuthorsArrayBuilder;
  
  private boolean isAddSynomys;
  
  private StringBuilder sb; 
  
  public JsonConverter() {

  }
   

  public List<JsonArray> convertCsvToJson(List<CSVRecord> records, JsonObject collectionJson) {  
    log.info("convertCsvToJson" );
 
    List<JsonArray> list = new ArrayList();
    arrayBuilder = Json.createArrayBuilder(); 
    builder = Json.createObjectBuilder();
    String collectionPrefix = collectionJson.getString(idPrefixKey);
    log.info("collection prefix : {}", collectionPrefix);
    String collectionName = collectionJson.getString(collectionNameKey);  
    JsonObject mappingJson = collectionJson.getJsonObject(mappingKey); 
    JsonObject submappingJson = mappingJson.getJsonObject(submippingKey);
     
    isAddSynomys = false; 
    AtomicInteger counter = new AtomicInteger(1);
    records.stream()
            .forEach(record -> { 
//              String catalogueId = record.get(catalogueIdKey).trim().replaceAll("^\"|\"$", "");
//              String catalogedDate = record.get(submappingJson.getString(catalogedDateKey)).replaceAll("^\"|\"$", "");
              String catalogueId = record.get(catalogueIdKey).trim();
              String catalogedDate = record.get(submappingJson.getString(catalogedDateKey));
              boolean isValid = validateCatalogId(catalogueId, catalogedDate);
              log.info("isvalid : {} -- {}", isValid, catalogueId);
              if (validateCatalogId(catalogueId, catalogedDate)) {
                if (counter.get() % batchSize == 0) {
                  list.add(arrayBuilder.build()); 
                  arrayBuilder = Json.createArrayBuilder();
                }

                counter.getAndIncrement();
                if (isAddSynomys) {
                  builder.add(synonymKey, synomysArrayBuilder);
                  builder.add(synonymAuthorKey, synomyAuthorsArrayBuilder);
                }
                arrayBuilder.add(builder);

                builder = Json.createObjectBuilder();
                synomysArrayBuilder = Json.createArrayBuilder();
                synomyAuthorsArrayBuilder = Json.createArrayBuilder();
                isAddSynomys = false;
                JsonHelper.getInstance().addId(builder, catalogueId, collectionPrefix);
                JsonHelper.getInstance().addAttribute(builder, collectionNameKey, collectionName);
                JsonHelper.getInstance().addAttribute(builder, collectionIdKey, collectionPrefix);

                mappingJson.keySet().stream()
                        .filter(key -> !key.equals(submippingKey))
                        .forEach(key -> {
                          add(key, record.get(mappingJson.getString(key)));
                        });
                addExtraData(submappingJson, record);
                String country = record.get(mappingJson.getString(countryKey));
                if (country != null && country.equals(sweden)) {
                  add(inSwedenKey, true);
                }
              } else { 
                if(isAddSynomys) { 
                  addSynomys(record.get(speciesName), record.get(author)); 
                } 
              }  
            });
    list.add(arrayBuilder.build());
    return list;
  }
  
  private void addExtraData(JsonObject json, CSVRecord record) {  
    String typeStatus = record.get(json.getString(typeStatusKey)); 
    
    if(!StringUtils.isBlank(typeStatus)) {
      add(isTypeKey, true);
      add(typeStatusKey, typeStatus);
    }
    
    addHighTaxon(json.getJsonObject(classificationKey), record);
    addMap(json.getJsonObject(coordinateKey), record);
    addCollectingDate(json.getJsonObject(collDateKey), record);
    addSynomy(json.getJsonObject(synonymKey), record);
  }
  
  private void addSynomys(String synomy, String synomyAuthor) {
    sb = new StringBuilder();
    sb.append(synomy);
    sb.append(emptySpace);
    sb.append(synomyAuthor); 
     
    if (!StringUtils.isBlank(synomy)) {
      synomysArrayBuilder.add(synomy); 
      synomyAuthorsArrayBuilder.add(sb.toString().trim()); 
    } 
  }
  
  private void addSynomy(JsonObject json, CSVRecord record) {
    String synomy = record.get(json.getString(synonymKey)); 
   
    if(!StringUtils.isBlank(synomy)) {
      addSynomys(synomy, record.get(json.getString(synonymAuthorKey))); 
      isAddSynomys = true;
    } 
  }
 
  private void addMap(JsonObject json, CSVRecord record) {
    String latitude = record.get(json.getString(latitudeKey));
    String longtitude = record.get(json.getString(longtitudeKey));
    
    if(!StringUtils.isAnyBlank(latitude, longtitude)) {
      if(pattern.matcher(latitude).matches() && pattern.matcher(longtitude).matches()) {
        JsonHelper.getInstance().addAttribute(builder, latitudeKey, latitude);
        JsonHelper.getInstance().addAttribute(builder, longtitudeKey, longtitude); 
        add(mapKey, true);
        addCoordinates(latitude, longtitude);
      } 
    }
  }
  
  private void addCoordinates(String latitude, String longtitude) {
    sb = new StringBuilder();
    sb.append(n);
    sb.append(latitude);
    sb.append(e);
    sb.append(longtitude);
    JsonHelper.getInstance().addAttribute(builder, coordinateKey, sb.toString().trim()); 
  }
  
  private void addHighTaxon(JsonObject json, CSVRecord record) { 
    sb = new StringBuilder();
    String species = record.get(json.getString(speciesKey));
    String genus = record.get(json.getString(genusKey));
    String family = record.get(json.getString(familyKey));
    if (!StringUtils.isBlank(species)) {
      add(txFullNameKey, species);
      json.keySet().stream()
              .filter(key -> !key.equals(speciesKey))
              .forEach(key -> {
                sb.append(record.get(json.getString(key)));
                sb.append(slash);
              });
    } else if (!StringUtils.isBlank(genus)) {
      add(txFullNameKey, genus);
      json.keySet().stream()
              .filter(key -> !(key.equals(speciesKey) || key.equals(genusKey)))
              .forEach(key -> {
                sb.append(record.get(json.getString(key)));
                sb.append(slash);
              });
    } else if (!StringUtils.isBlank(family)) {
      add(txFullNameKey, family);
      json.keySet().stream()
              .filter(key -> !(key.equals(speciesKey) || key.equals(genusKey) || key.equals(familyKey)))
              .forEach(key -> {
                sb.append(record.get(json.getString(key)));
                sb.append(slash);
              });
    } 
    add(higherTxKey, StringUtils.removeEnd(sb.toString().trim(), slash));
  }

  private void addCatalogDate(LocalDate date, String catalogedDate) { 
    JsonHelper.getInstance().addAttribute(builder, catalogedDateKey, catalogedDate);  
    JsonHelper.getInstance().addAttribute(builder, catalogedMonthKey, date.getMonthValue());  
    JsonHelper.getInstance().addAttribute(builder, catalogedMonthNameKey, date.getMonth().name()); 
    JsonHelper.getInstance().addAttribute(builder, catalogedYearKey, date.getYear());  
  }

  private void addCollectingDate(JsonObject json, CSVRecord record) {
    String collYear = record.get(json.getString(collYearKey));
    String collMonth = record.get(json.getString(collMonthKey));
    String collDay = record.get(json.getString(collDayKey));

    if (!StringUtils.isAnyBlank(collYear, collMonth, collDay)) { 
      sb = new StringBuilder(); 
      sb.append(collYear);
      sb.append(dash);
      sb.append(collMonth); 
      sb.append(dash);
      sb.append(collDay); 
      add(startDateKey, sb.toString().trim());
    } else {
      if(!StringUtils.isBlank(collYear)) {
        sb = new StringBuilder(); 
        sb.append(collYear);
        sb.append(dash);
        if(!StringUtils.isBlank(collMonth)) { 
          sb.append(collMonth); 
        } else { 
          sb.append(strOne); 
        }
        sb.append(dash);
        if(!StringUtils.isBlank(collDay)) {
          sb.append(collDay); 
        } else {
          sb.append(strOne); 
        }
      }
    }
  }
  
  private boolean validateCatalogId(String value, String catalogedDate) { 
    log.info("validateCatalogId : {} -- {}", value, catalogedDate);
    if(!StringUtils.isAllBlank(value, catalogedDate)) {
      LocalDate date = Util.getInstance().stringToLocalDate(catalogedDate); 
      if(date != null) { 
        addCatalogDate(date, catalogedDate);
        return true;
      } 
    } 
    return false; 
  } 
 
  private void add(String att, String value) { 
//    log.info("add : {} -- {}", att, value);
    if(!StringUtils.isBlank(value)) {
      builder.add(att, value);
    } 
  }

  private void add(String att, boolean value) {
//    log.info("add : {} -- {}", att, value); 
    builder.add(att, value);
  }
  
  private void add(String att, int value) {
//    log.info("add : {} -- {}", att, value); 
    builder.add(att, value);
  }
  
  public JsonObject getCollectionJson(String mappingFile, String collection) {
    log.info("getCollectionJson" );
    return read(mappingFile)
            .getValuesAs(JsonObject.class)
            .stream()
            .filter(j -> j.getString(collectionNameKey).equals(collection))
            .findAny()
            .orElse(null);  
  }
  
  private JsonArray read(String mappingFilePath) {
    log.info("read"); 
    InputStream fis = null;
    try {
      fis = new FileInputStream(mappingFilePath);  
      JsonArray array = Json.createReader(fis).readArray();    
      return array;
    } catch (FileNotFoundException ex) {
      log.error(ex.getMessage()); 
    } finally {
      try {
        if(fis != null) {
           fis.close();
        } 
      } catch (IOException ex) {
        log.error(ex.getMessage());
      }
    }
    return null;
  }



}
