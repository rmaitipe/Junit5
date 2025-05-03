package test.parameterized;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ZipCodePair;
import com.ZipLimiter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/*
 * This class is to Unit test the ZipLimiter class. 
 */
public class ZipLimiterTest2 {

	private static Log log = LogFactory.getLog(ZipLimiterTest2.class);

	public static List<Object[]> fileNames() {
      return Arrays.asList(new Object[][] {
         { "resources/test/zipInputNoConflictPairs.txt", "resources/test/expectedv2/expectedZipInputNoConflictPairs.txt" },
         { "resources/test/zipInputMergeSortedPairs.txt", "resources/test/expectedv2/expectedZipInputMergeSortedPairs.txt"},
         { "resources/test/zipInputMergeUnsortedPairs.txt", "resources/test/expectedv2/expectedZipInputMergeUnsortedPairs.txt" },
         { "resources/test/zipInputBadDataPairs.txt", "resources/test/expectedv2/expectedZipInputBadDataPairs.txt"}
      });
    }

	/*
	* These are the values to be compared against the data read from files in test case scenarios.
	* Called automatically before the Test class is run.
	*/
 	@BeforeEach
    public void setUp() {
 		log.info("@Before - setUp");
    }
    
    @AfterEach
    public void tearDown() {
    	log.info("@After - tearDown");
    }
    
    /*
    * Runs test cases for different scenarios comparing size of the lists and content of the lists
    * Test cases: No Merge, Sorted Merge, UnSorted Merge, Merge With BadData
    *
    */
	@ParameterizedTest
	@MethodSource("fileNames")
	public void zipLimiterDataTest(String inputFile, String outputFile) {
		ZipLimiter zip = new ZipLimiter();
		List<ZipCodePair> testDataSet = zip.test(inputFile);
		List<ZipCodePair> outputDataSet = read(outputFile);
		Assertions.assertEquals(outputDataSet, testDataSet);
	}
    
    /*
     * Returns the compressed List<ZipCodePair>.
     *
     * @param  String The location of the text file with input zip ranges.
     * @return List<Pair>  list of zip codes
     */
 	public List<ZipCodePair> read(String fileInput){
 		int lineNumber=0;
 		List<ZipCodePair> zipOutputMatchList = new ArrayList<>();
         	//try with resources
         	try (BufferedReader br = new BufferedReader(new FileReader(fileInput))){
             String line = null;
             log.info("Reading and Validating contents of file  :" +fileInput);
             while((line = br.readLine()) != null){
             	 lineNumber++;
                  String [] strArray =line.split(",");
                  if (strArray[0].length()==6 && strArray[1].length()==6){
                	  String zipA =strArray[0].substring(1, 6);
                	  String zipB =strArray[1].substring(0, 5);
              	 	  ZipCodePair pair= new ZipCodePair(Integer.parseInt(zipA),Integer.parseInt(zipB));
              	 	  zipOutputMatchList.add(pair);
                  } else{
                	  log.info("Error at line number  : "+lineNumber+" Input is not in a 5 digit format");
                  }
             }
        } catch (IOException e) {
			log.error(e);
        }
 	    return zipOutputMatchList;
 	}

}
