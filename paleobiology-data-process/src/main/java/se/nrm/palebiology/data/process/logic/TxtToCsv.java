package se.nrm.palebiology.data.process.logic;

import com.aspose.cells.Workbook;
import java.io.Serializable; 
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */

@Slf4j
public class TxtToCsv implements Serializable {
  
  public TxtToCsv() {
    
  }
  
  public void convert() {
 
    try {
      Workbook workbook = new Workbook("/Users/idali/Downloads/exportPAL.txt");
      workbook.save("/Users/idali/Downloads/test.csv");
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
	
  }
}
