package sk.folki.aspectlogger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

public class LogErrorMessageOfLoggableMethodEndToEndTest extends AbstractEndToEndTest {
	private final static String MESSAGE = "Service operation.";
	private Logger log;
	private Service service;
	
	public LogErrorMessageOfLoggableMethodEndToEndTest() {
		service = getBean(Service.class);
		log = getLoggerWithDefaultConfiguration(ServiceImpl.class);
		configureLogger(ServiceImpl.class);
	}

	@Override
	protected Class<?>[] getServiceBeansConfig() {
		return new Class[] {ServiceImpl.class};
	}
	
	@Test
	public void testLogErrorMessageWhenLoaggableMethodThrowsException() {
		givenServiceLoggerIsSetToLevel(Level.INFO);
		givenServiceLoggerAppenderUsesSimpleLayout();
		givenServiceMethodDefinesLoggableMessageAs("Service operation.");
		whenServiceMethodIsInvoked();
		thenLogShouldContainTheOnlyRecord("ERROR - Error: Service operation.");
	}
	
	private void givenServiceLoggerIsSetToLevel(Level logLevel) {
		setLoggerToLogLevel(log, logLevel);
	}
	
	private void givenServiceLoggerAppenderUsesSimpleLayout() {
		setSimpleLayoutAppenderToLogger(log);
	}
	
	private void givenServiceMethodDefinesLoggableMessageAs(String expectedMessage) {
		// Loggable annotation value should be set to the value that is passed to this method
		assertThat(MESSAGE, equalTo(expectedMessage));
	}
	
	private void whenServiceMethodIsInvoked() {
		try {
			service.operation();
		} catch (Exception e) {}
	}
	
	private void thenLogShouldContainTheOnlyRecord(String expectedLogRecord) {
		StringBuffer logMessages = getLogMessages();
		int logMessagesCount = getLogMessagesCount(logMessages);
		assertThat(logMessagesCount, is(1));
		assertTrue(logMessageContainsMessage(logMessages, expectedLogRecord));
	}	
	
	static interface Service {
		void operation();
	}
	
	static class ServiceImpl implements Service {
		
		@Override
		@Loggable(MESSAGE)
		public void operation() {
			throw new RuntimeException("Service operation failure.");
		}
	}
}
