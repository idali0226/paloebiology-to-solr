/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.palebiology.data.process;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
import se.nrm.palebiology.data.process.logic.util.Util;

/**
 *
 * @author idali
 */
public class Main {

  private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
  

  public static void main(String[] args) {
    

      String date = "1960-18-05";
      LocalDate localDate = LocalDate.parse(date); 
      
//      String isValid = Util.getInstance().validateDate(date);
//      System.out.println("isValid : " + isValid);
//      LocalDate localDate = LocalDate.parse(date); 
//      System.out.println("localDate..." + localDate);

//    String strNum = "pencil:programmer";
//    
//    
//    boolean isTrueu = Pattern.compile("[^A-Za-z0-9 ]").matcher(strNum).find(); 
// 
//     
//    boolean b = strNum.matches("[0-9]+");
//     System.out.println("is true ? " + b + "..." + isTrueu);

      
//    System.out.println("is true: " + pattern.matcher(strNum).matches());
    
//    System.out.println("any space: " + strNum.matches(".*\\s.*"));
    
  }

}
