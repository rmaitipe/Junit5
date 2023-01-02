package test.basic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

public class MessageServiceTest {

	private static Log log = LogFactory.getLog(MessageServiceTest.class);


	@DisplayName("Test MessageService.get()")
	@Test
	void testGet() {
		assertEquals("Hello JUnit 5", MessageService.get());
	}

	@DisplayName("Run this if `assumeTrue` condition is true, else aborting this test (Custom Message)")
	@Test
	void testOnlyOnDevEnvElseAbortWithCustomMsg() {
		assumeTrue("DEV".equals(System.getenv("APP_MODE")), () -> "Aborting test: not on developer environment");
		assertEquals(2, 1 + 1);
	}

	@Test
	void testAssumingThat() {
		assertEquals(2, 1 + 1);
		// run this only if assumingThat condition is true
		assumingThat("DEV".equals(System.getenv("APP_MODE")),
				() -> {
					assertEquals(2, 1 + 1);
				});
		assertEquals(2, 1 + 1);
	}


}
