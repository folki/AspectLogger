package sk.folki.aspectlogger;

import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

abstract public class AbstractEndToEndTest {
	private StringWriter logFileContent = new StringWriter();
	private static AnnotationConfigApplicationContext springContext;
	
	/**
	 * @return either @Configuration annotated classes or individual services that are being managed by Spring.
	 */
	protected abstract Class<?>[] getServiceBeansConfig();
		
	public AbstractEndToEndTest() {
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
	}

	private void configureRootLogger() {
		Logger rootLogger = Logger.getRootLogger();
		setSimpleLayoutAppenderToLogger(rootLogger);
		setLoggerToLogLevel(rootLogger, Level.OFF);
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
		springContext.register(getServiceBeansConfig());
	}

	private void givenSpringApplicationContextIsInitialized() {
		springContext.refresh();
	}
	
	protected <T> T getBean(Class<T> beanClass) {
		return springContext.getBean(beanClass);
	}
	
	protected Logger getLoggerWithDefaultConfiguration(Class<?> loggerName) {
		configureLogger(loggerName);
		return Logger.getLogger(loggerName);
	}
	
	protected void configureLogger(Class<?> loggerName) {
		Logger log = Logger.getLogger(loggerName);
		log.setAdditivity(false);
		setSimpleLayoutAppenderToLogger(log);		
		setLoggerToLogLevel(log, Level.OFF);
	}

	protected void setLoggerToLogLevel(Logger logger, Level logLevel) {
		logger.setLevel(logLevel);
	}

	protected void setSimpleLayoutAppenderToLogger(Logger logger) {
		logger.removeAllAppenders();
		WriterAppender appender = new WriterAppender(new SimpleLayout(), logFileContent);
		appender.setWriter(logFileContent);
		logger.addAppender(appender);
	}
	
	protected StringBuffer getLogMessages() {
		return logFileContent.getBuffer();
	}
	
	protected boolean logMessageContainsMessage(StringBuffer logMessages, String expectedMessage) {
		if (logMessages.indexOf(expectedMessage) == -1) {
			return false;
		} else {
			return true;
		}
	}
	
	protected int getLogFileLenght() {
		return getLogMessages().length();
	}
	
	protected int getLogMessagesCount(StringBuffer logMessages) {
		return logMessages.toString().split("\n").length;
	}
}