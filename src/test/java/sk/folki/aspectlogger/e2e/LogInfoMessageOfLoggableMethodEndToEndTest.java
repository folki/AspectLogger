package sk.folki.aspectlogger.e2e;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import sk.folki.aspectlogger.client.Loggable;

public class LogInfoMessageOfLoggableMethodEndToEndTest extends AbstractEndToEndTest {
	private Logger log;
	private Service service;
	private static final String MESSAGE = "Service operation.";
	
	public LogInfoMessageOfLoggableMethodEndToEndTest() {
		service = getBean(Service.class);
		log = getLoggerWithDefaultConfiguration(ServiceImpl.class);
	}
	
	@Override
	protected Class<?>[] getServiceBeansConfig() {
		return new Class[] {ServiceImpl.class};
	}
	
	@Test
	public void testLogMessage() {
		givenServiceLoggerIsSetToLevel(Level.INFO);
		givenServiceLoggerAppenderUsesSimpleLayout();
		givenServiceMethodDefinesLoggableMessageAs("Service operation.");
		whenServiceMethodIsInvoked();
		thenLogShouldContainTheRecord("INFO - Service operation.");
	}

	private void givenServiceLoggerAppenderUsesSimpleLayout() {
		setSimpleLayoutAppenderToLogger(log);
	}

	private void givenServiceMethodDefinesLoggableMessageAs(String expectedMessage) {
		// Loggable annotation value should be set to the value that is passed to this method
		assertThat(MESSAGE, equalTo(expectedMessage));
	}

	private void givenServiceLoggerIsSetToLevel(Level logLevel) {
		setLoggerToLogLevel(log, logLevel);
	}

	private void whenServiceMethodIsInvoked() {
		service.operation();
	}

	private void thenLogShouldContainTheRecord(String expectedLogRecord) {
		StringBuffer logMessages = getLogMessages();
		assertTrue(logMessageContainsMessage(logMessages, expectedLogRecord));
	}	
	
	@Test
	public void testDoNotLogMessageWhenLogLevelIsHigherThanInfo() {
		givenServiceLoggerIsSetToLevel(Level.WARN);
		givenServiceLoggerAppenderUsesSimpleLayout();
		givenServiceMethodDefinesLoggableMessageAs("Service operation.");
		whenServiceMethodIsInvoked();
		thenLogShouldNotContainAnyRecord();
	}

	private void thenLogShouldNotContainAnyRecord() {
		int logLength = getLogFileLenght();
		assertThat(logLength, is(0));
	}
	
	static interface Service {
		void operation();
	}
	
	static class ServiceImpl implements Service {
		
		@Override
		@Loggable(MESSAGE)
		public void operation() {		
		}		
	}
}
