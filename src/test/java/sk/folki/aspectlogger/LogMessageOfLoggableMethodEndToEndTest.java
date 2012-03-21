package sk.folki.aspectlogger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class LogMessageOfLoggableMethodEndToEndTest {
	private Logger log;
	private StringWriter logFileContent = new StringWriter();
	private Service service;
	private static final String MESSAGE = "Service operation.";
	
	private static AnnotationConfigApplicationContext springContext;
	
	@Before
	public void init() {
		givenPrerequisitiesForUsageAspectLoggerLibraryAreFulfilled();		
	}
	
	private void givenPrerequisitiesForUsageAspectLoggerLibraryAreFulfilled() {
		givenLog4jIsConfigured();
		givenSpringIsUsed();
		givenLogAspectIsManagedBySpring();
		givenSpringAspectJAutoProxyIsEnabled();
		givenServiceBeanIsManagedBySpring();
		givenSpringApplicationContextIsInitialized();		
	}
	
	private void givenLog4jIsConfigured() {
		configureRootLogger();
		configureServiceLogger();
	}

	private void configureRootLogger() {
		Logger rootLogger = Logger.getRootLogger();
		setSimpleLayoutAppenderToLogger(rootLogger);
		setLoggerToLogLevel(rootLogger, Level.OFF);
	}

	private void configureServiceLogger() {
		log = Logger.getLogger(ServiceImpl.class);
		log.setAdditivity(false);
		setSimpleLayoutAppenderToLogger(log);		
		setLoggerToLogLevel(log, Level.OFF);
	}

	private void setLoggerToLogLevel(Logger logger, Level logLevel) {
		logger.setLevel(logLevel);
	}

	private void setSimpleLayoutAppenderToLogger(Logger logger) {
		logger.removeAllAppenders();
		WriterAppender appender = new WriterAppender(new SimpleLayout(), logFileContent);
		appender.setWriter(logFileContent);
		logger.addAppender(appender);
	}

	private void givenSpringIsUsed() {
		springContext = new AnnotationConfigApplicationContext();
	}

	private void givenLogAspectIsManagedBySpring() {
		springContext.register(LogAspect.class);
	}

	private void givenSpringAspectJAutoProxyIsEnabled() {
		springContext.register(org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator.class);	// 	<aop:aspectj-autoproxy/>
	}

	private void givenServiceBeanIsManagedBySpring() {		
		springContext.register(ServiceBeansConfig.class);
	}

	private void givenSpringApplicationContextIsInitialized() {
		springContext.refresh();
		service = springContext.getBean(Service.class);
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
		StringBuffer logMessages = logFileContent.getBuffer();
		assertTrue(logMessageContainsMessage(logMessages, expectedLogRecord));
	}
	
	private boolean logMessageContainsMessage(StringBuffer logMessages, String expectedMessage) {
		if (logMessages.indexOf(expectedMessage) == -1) {
			return false;
		} else {
			return true;
		}
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

	private int getLogFileLenght() {
		return logFileContent.getBuffer().length();
	}

	@Configuration
	static class ServiceBeansConfig {
		
		@Bean
		Service getService() {
			return new ServiceImpl();
		}
		
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
