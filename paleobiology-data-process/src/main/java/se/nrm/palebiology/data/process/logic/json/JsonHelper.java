package se.nrm.palebiology.data.process.logic.json;

import java.math.BigDecimal;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import se.nrm.palebiology.data.process.logic.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class JsonHelper {
  
  
  private static JsonHelper instance = null;
  
  private final String idKey = "id";
  private final String underScore = "_";
//   private final String collectionNameKey = "collectionName";
  private StringBuilder sb;

  public static JsonHelper getInstance() {
    synchronized (JsonHelper.class) {
      if (instance == null) {
        instance = new JsonHelper();
      }
    }
    return instance;
  }
  
  public void addId(JsonObjectBuilder attBuilder, String value, String prefix) {
    sb = new StringBuilder();
    sb.append(prefix);
    sb.append(underScore);
    sb.append(value);
    attBuilder.add(idKey, sb.toString().trim());
  }

//  public void addCollectionName(JsonObjectBuilder attBuilder, JsonObject json) {
//    String collectionName = json.getString(collectionNameKey);
//    log.info(collectionName);
//    attBuilder.add(collectionNameKey, collectionName);
//  }
  
  public void addAttribute(JsonObjectBuilder attBuilder, String key, String value) { 
    attBuilder.add(key, value.trim());
  }
  
  /**
   * 
   * @param attBuilder
   * @param key
   * @param type
   * @param value 
   */
  public void addAttribute(JsonObjectBuilder attBuilder, String key, String type, String value) { 
    
    if (value != null && !StringUtils.isEmpty(value)) {
      try {
        switch (type) {
          case "String":
            attBuilder.add(key, value.trim());
            break;
          case "date":
            attBuilder.add(key, Util.getInstance().validateDate(value.trim())); 
            break;
          case "int":
            attBuilder.add(key, Integer.parseInt(value));
            break;
          case "Short":
            attBuilder.add(key, Short.valueOf(value));
            break;
          case "BigDecimal": 
            if(isValidNumber(key, value)) {
              attBuilder.add(key, new BigDecimal(value)); 
            } 
            break;
          case "boolean":
            attBuilder.add(key, Boolean.valueOf(value));
            break;
          case "double":
            attBuilder.add(key, Double.parseDouble(value));
            break;
          case "float":
            attBuilder.add(key, Float.parseFloat(value));
            break;
          case "long":
            attBuilder.add(key, Long.parseLong(value));
            break;
          default:
            attBuilder.add(key, value.trim());
            break;
        }
      } catch (NumberFormatException e) {

      } 
    }
  }
    
  private boolean isValidNumber(String key, String value) {
    switch (key) {
      case "decimalLatitude":
        return Math.abs(Double.parseDouble(value)) <= 90;
      case "decimalLongitude":
        return Math.abs(Double.parseDouble(value)) <= 180;
      default:
        return true;
    }
  } 
}
