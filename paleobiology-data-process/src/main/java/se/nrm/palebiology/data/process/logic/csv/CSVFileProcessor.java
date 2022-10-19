package se.nrm.palebiology.data.process.logic.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.List; 
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

/**
 *
 * @author idali
 */ 
@Slf4j
public class CSVFileProcessor implements Serializable {
  
  private final char tabSepartor = ';';
  
  public CSVFileProcessor() {

  }

  public List<CSVRecord> readCsvFile(String filePath) {
    log.info("readCsvFile : {}", filePath);

    CSVFormat csvFileFormat;
    csvFileFormat = CSVFormat.newFormat(tabSepartor)
            .withFirstRecordAsHeader()
            .withQuoteMode(QuoteMode.ALL);

    File file = new File(filePath);
    Reader reader = null;
    try {
      reader = new FileReader(file);
      return csvFileFormat.parse(reader).getRecords(); 
    } catch (FileNotFoundException ex) {
      log.error(ex.getMessage());
    } catch (IOException ex) {
      log.error(ex.getMessage());
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException ex) {
        log.error(ex.getMessage());
      }
    }
    return null; 
  }
}
