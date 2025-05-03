package test.parameterized;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import com.ZipLimiter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ZipCodePair;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.ThrowingConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/*
 * This class is to Unit test the ZipLimiter class. 
 */
public class ZipLimiterTest3 {

	private static Log log = LogFactory.getLog(ZipLimiterTest3.class);


	@TestFactory
	DynamicTest testZipRead() throws Exception {
		String fileInputStream = "resources/test/zipInputNoConflictPairs.txt";
		String fileOutputStream = "resources/test/expectedv2/expectedZipInputNoConflictPairs.txt";
		ZipLimiter zip = new ZipLimiter();
		List<ZipCodePair> testDataSet = zip.test(fileInputStream);
		List<ZipCodePair> outputPair = read(fileOutputStream);

		return dynamicTest("Test zip merge of file",() -> assertEquals(testDataSet, outputPair));
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

	@TestFactory
	Stream<DynamicTest> generateRandomNumberOfTests() {

		// Generates random positive integers between 0 and 100 until
		// a number evenly divisible by 7 is encountered.
		Iterator<Integer> inputGenerator = new Iterator<Integer>() {

			Random random = new Random();
			int current;

			@Override
			public boolean hasNext() {
				current = random.nextInt(100);
				return current % 7 != 0;
			}

			@Override
			public Integer next() {
				return current;
			}
		};

		// Generates display names like: input:5, input:37, input:85, etc.
		Function<Integer, String> displayNameGenerator = (input) -> "input:" + input;

		// Executes tests based on the current input value.
		ThrowingConsumer<Integer> testExecutor = (input) -> assertTrue(input % 7 != 0);

		// Returns a stream of dynamic tests.
		return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
	}

}
