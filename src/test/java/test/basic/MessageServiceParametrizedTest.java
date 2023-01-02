package test.basic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.junit.platform.commons.util.StringUtils;

import java.time.Month;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class MessageServiceParametrizedTest {

	private static Log log = LogFactory.getLog(MessageServiceParametrizedTest.class);

	@ParameterizedTest
	@ValueSource(strings = {"", "  "})
	void isBlank_ShouldReturnTrueForNullOrBlankStrings(String input) {
		assertTrue(StringUtils.isBlank(input));
	}

	@ParameterizedTest(name = "#{index} - Test with TimeUnit: {0}")
	@EnumSource(value = TimeUnit.class, names = {"MINUTES", "SECONDS"})
	void test_timeunit_ok(TimeUnit time) {
	}

	/*
	We can filter out a few months by using the names attribute.
	We could also assert the fact that April, September, June and November are 30 days long:
	We can pass a regular expression to the names attribute
	*/
	@ParameterizedTest
	@EnumSource(value = Month.class,
			names = {"APRIL", "JUNE", "SEPTEMBER", "NOVEMBER"})
			//mode = EnumSource.Mode.EXCLUDE
			//names = ".+BER", mode = EnumSource.Mode.MATCH_ANY)
	void someMonths_Are30DaysLong(Month month) {
		final boolean isALeapYear = false;
		assertEquals(30, month.length(isALeapYear));
	}

	@ParameterizedTest(name = "#{index} - Test with {0} and {1}")
	@MethodSource("argumentProvider")
	void test_method_multi(String str, int length) {
	}
	static Stream<Arguments> argumentProvider() {
		return Stream.of(arguments("abc", 3), arguments("lemon", 2)
		);
	}
	@ParameterizedTest
	@CsvSource({"test,TEST", "tEst,TEST", "Java,JAVA"})
	//@CsvSource(value = {"test:test", "tEst:test", "Java:java"}, delimiter = ':')
	void toUpperCase_ShouldGenerateTheExpectedUppercaseValue(String input, String expected) {
		String actualValue = input.toUpperCase();
		assertEquals(expected, actualValue);
	}

	//Instead of passing the CSV values inside the code, we can refer to an actual CSV file.
	@ParameterizedTest
	@CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
	void toUpperCase_ShouldGenerateTheExpectedUppercaseValueCSVFile(
			String input, String expected) {
		String actualValue = input.toUpperCase();
		assertEquals(expected, actualValue);
	}


}
