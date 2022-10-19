package se.nrm.palebiology.data.process.logic.util; 

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author idali
 */
public class Util {
   
//  private final String and = "&";
//  private final String page = "Page";
//  private final String equalSign = "=";
//  private final String underscore = "_";
  private final String dash = "-";
//  private final String csv = ".csv";
  private final String one = "01";
  private final String zero = "0";
  
//  private final String institutionKey = "institution";
//  private final String mappingKey = "mapping";
  
  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  

  private static Util instance = null;

  public static Util getInstance() {
    synchronized (Util.class) {
      if (instance == null) {
        instance = new Util();
      }
    }
    return instance;
  }
 
  public LocalDate stringToLocalDate(String strDate) {
    try {
      return LocalDate.parse(strDate); 
    } catch(DateTimeException ex) {
      return null;
    } 
  }
  
  public String validateDate(String strDate) {
    boolean isValidate = isValidDate(strDate);
    return isValidate ? strDate : fixInvalidDate(strDate);
  }
   
  private boolean isValidDate(String strDate) {
    if(strDate == null) {
      return false;
    } 
    try {
      dateFormat.parse(strDate);
      return true;
    } catch (ParseException e) { 
      return false;
    }  
  }
  
  private String fixInvalidDate(String strDate) { 
    String[] array = strDate.split(dash);
    
    String year;
    String month;
    String day;
    switch (array.length) {
      case 3:
        year = array[0];
        month = array[1];
        day = array[2]; 
        break;
      case 2:
        year = array[0];
        month = array[1];
        day = one;
        break;
      default:
        year = array[0];
        month = one;
        day = one;
        break;
    } 
    month = month.length() == 1 ? padZero(month) : month;
    day = day.length() == 1 ? padZero(day) : day;
    String[] strArray = {year, month, day};
    return String.join(dash, strArray);
  }
  
  private String padZero(String value) {
    return StringUtils.leftPad(value, 2, zero);
  }
//  

//   
//  public Date stringToDate(String strDate) { 
//    if(strDate == null) {
//      return null;
//    } 
//    try {
//      return dateFormat.parse(strDate);
//    } catch (ParseException e) { 
//      return null;
//    }
//  }
//  
//  public String buildId(String catalogNumber, String institutionCode) {
//    StringBuilder idSb = new StringBuilder();
//    idSb.append(institutionCode);
//    idSb.append(dash);
//    idSb.append(catalogNumber);
//    return idSb.toString().trim();
//  }
//  
//  public String buildCsvDownloadUrl(String dataSourcePath, String institution, int numOfPage) {
//    StringBuilder dataSourceSb = new StringBuilder();
//    dataSourceSb.append(dataSourcePath);
//    dataSourceSb.append(dataSourceInstitution);
//    dataSourceSb.append(institution);
//    dataSourceSb.append(and);
//    dataSourceSb.append(page);
//    dataSourceSb.append(equalSign);
//    dataSourceSb.append(numOfPage);
//    
//    return dataSourceSb.toString().trim();
//  }
//  
//  public String buildCsvFilePath(String csvFile, String institution, int numOfPage) {
//    StringBuilder csvFileSb = new StringBuilder();
//    csvFileSb.append(csvFile);
//    csvFileSb.append(institution);
//    csvFileSb.append(underscore);
//    csvFileSb.append(page);
//    csvFileSb.append(numOfPage);
//    csvFileSb.append(csv);
//    return csvFileSb.toString().trim();
//  }
}
