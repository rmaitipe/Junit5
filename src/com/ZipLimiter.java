package com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/*
 * This class uses the zipInputPair.txt as a source to read a zip code range and then compresses the range where values overlap 
 * Values in the file are assumed to be in comma separated pairs
 */
public class ZipLimiter {
	
	private List<ZipCodePair> zipOutputMatchList;
	private List<ZipCodePair> zipInputMatchList;
	private String zipA;
	private String zipB;
	private static String fileInput ="resources/zipInputPair.txt";
	private int lineNumber;
	private static Log logger = LogFactory.getLog(ZipLimiter.class);
	
    /*
    * Returns the compressed List<Pair> after combining overlapping ranges.
    *
    * @param  String The location of the text file with input zip ranges.
    * @return List<Pair> merged list of zip codes after combining overlapping ranges
    */
	public List<ZipCodePair> test(String fileInput){
		lineNumber=0;
        zipOutputMatchList = new ArrayList<ZipCodePair>();
        zipInputMatchList = new ArrayList<ZipCodePair>();
    	//try with resources
    	try (BufferedReader br = new BufferedReader(new FileReader(fileInput))){
	        String line = null;
	        logger.info("Reading and Validating contents of file  :" +fileInput);
	        while((line = br.readLine()) != null){
	        	 lineNumber++;
	             String [] strArray =line.split(",");
	             if (strArray[0].length()==6 && strArray[1].length()==6){
	             zipA =strArray[0].substring(1, 6);
	             zipB =strArray[1].substring(0, 5);
	         	 	if (!validateStop(zipA) && !validateStop(zipB)){
	         	 		ZipCodePair pair= new ZipCodePair(Integer.parseInt(zipA),Integer.parseInt(zipB));
	         	 		zipInputMatchList.add(pair);
	         	 	}
	             } else{
	            	 logger.info("Error at line number  : "+lineNumber+" Input is not in a 5 digit format");
	             }
	        }
        } catch (IOException e) {
			logger.error(e.getMessage());
        }
	    zipInputMatchList.sort(Comparator.comparing(ZipCodePair::getZipA));
	    zipOutputMatchList =mergeBoundries(zipInputMatchList);
	    zipOutputMatchList.forEach(item->System.out.println(item));
	    return zipOutputMatchList;
	}
	
    /*
    * Indicates if the input from file is a valid integer along with the line number where the failure occurred
    *
    * @param  String The input is a zip code.
    * @return boolean Fails validation if not an Integer, Not 5 digits
    */
	private boolean validateStop(String zip) {
		boolean isStop = false;
		try{
			Integer.parseInt(zip);
		}
		catch(NumberFormatException e){
			logger.error("Error at line number  : "+lineNumber+" Input is not a valid number");
			isStop =true;
		}
		if(zip.length()!=5 && !isStop){
			logger.error("Error at line number  : "+lineNumber+" Input is not a 5 digit zip code");
			isStop =true;
		}
		return isStop;
	}

    /*
    * Iterate and merge the zip codes where overlap exists
    *
    * @param  List<Pair> This input list consists of Pair values created from data read from file.
    * @return List<Pair> This output list consists of the merged Pair values.
    */
	private List<ZipCodePair> mergeBoundries(List<ZipCodePair> zipList) {
		for (ZipCodePair current: zipList){
			//new range is out of existing range, add it
			if (zipOutputMatchList.isEmpty()){
				zipOutputMatchList.add(current);
			} else{
				ZipCodePair prevPair =zipOutputMatchList.get(zipOutputMatchList.size()-1);
				if(prevPair.getZipA()<=current.getZipA() && prevPair.getZipB()>=current.getZipB()){
					//already included
				} else if(prevPair.getZipA()>current.getZipB() || prevPair.getZipB()<current.getZipA()){
					zipOutputMatchList.add(current);//New Range
				} else{
					ZipCodePair temp =new ZipCodePair(prevPair.getZipA(),current.getZipB());
					zipOutputMatchList.remove(zipOutputMatchList.size()-1);
					zipOutputMatchList.add(temp);
				}
			}
		}
		return zipOutputMatchList;
	}

	public static void main(String args[]){
		ZipLimiter zip = new ZipLimiter();
		zip.test(fileInput);
	}
	
    /*
    * Returns the compressed List<Pair> after combining overlapping ranges.
    *
    * @param  String with input zip ranges.
    * @return List<Pair> merged list of zip codes after combining overlapping ranges
    */
	public List<ZipCodePair> testv3(String line){
        zipOutputMatchList = new ArrayList<>();
        zipInputMatchList = new ArrayList<>();
   	  	String [] strArray1 =line.split("\\|");
   	  	for (String s:strArray1) {
   	  		String [] strArray2 =s.split(",");
   	  		if (strArray2[0].length()==6 && strArray2[1].length()==6){
   	  			zipA =strArray2[0].substring(1, 6);
   	  			zipB =strArray2[1].substring(0, 5);
   	  			if (!validateStop(zipA) && !validateStop(zipB)){
   	  				ZipCodePair pair= new ZipCodePair(Integer.parseInt(zipA),Integer.parseInt(zipB));
   	  				zipInputMatchList.add(pair);
   	  			}
   	  		} else{
   	  			System.out.println("Error at line number  : "+lineNumber+" Input is not in a 5 digit format");
   	  		}
   	  	}
	    zipInputMatchList.sort(Comparator.comparing(ZipCodePair::getZipA));
	    zipOutputMatchList =mergeBoundries(zipInputMatchList);
	    zipOutputMatchList.forEach(item->System.out.println(item));
	    return zipOutputMatchList;
	}
	
    /*
     * Returns the compressed List<Pair>.
     *
     * @param  String with input zip ranges.
     * @return List<Pair>  list of zip codes
     */
 	public List<ZipCodePair> parsev3(String line){
 	  List<ZipCodePair> zipOutputMatchList = new ArrayList<>();
 	  String [] strArray1 =line.split("|");
 	  for (String s:strArray1) {
 		  String [] strArray2 =s.split(",");
          if (strArray2[0].length()==6 && strArray2[1].length()==6){
        	  String zipA =strArray2[0].substring(1, 6);
        	  String zipB =strArray2[1].substring(0, 5);
      	 	  ZipCodePair pair= new ZipCodePair(Integer.parseInt(zipA),Integer.parseInt(zipB));
      	 	  zipOutputMatchList.add(pair);
          } else{
         	 System.out.println("Error at line : "+line+" Input is not in a 5 digit format");
          }
 	  }
 	  return zipOutputMatchList;
 	}
}
