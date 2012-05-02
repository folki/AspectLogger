package sk.folki.aspectlogger.e2e;

import static org.junit.Assert.fail;

import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import sk.folki.aspectlogger.client.LoggableAspect;

abstract class AbstractEndToEndTest {
	final static MavenDependency ASPECTJWEAVER = maven(groupId("org.aspectj"), artifactId("aspectjweaver"));
	final static MavenDependency ASPECTJRT = maven(groupId("org.aspectj"), artifactId("aspectjrt"));
	
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
		givenLoggableAspectIsManagedBySpring();
		givenSpringAspectJAutoProxyIsEnabled();
		givenServiceBeanIsManagedBySpring();
		givenDependencyIsOnTheApplicationClasspath(ASPECTJWEAVER);
		givenDependencyIsOnTheApplicationClasspath(ASPECTJRT);
		givenSpringApplicationContextIsInitialized();		
	}
	
	private void givenDependencyIsOnTheApplicationClasspath(MavenDependency dependency) {				
		if (dependency.equals(ASPECTJWEAVER)) {
			checkAspectJWeaverExistsOnClasspath();
		}
		if (dependency.equals(ASPECTJRT)) {
			checkAspectJRtExistsOnClasspath();
		}
	}
	
	private void checkAspectJRtExistsOnClasspath() {
		if (isNotClassOnClasspath("org.aspectj.lang.annotation.Aspect")) {
			fail("AspectJRT dependency is not safisfied.");
		}
	}
	
	private boolean isNotClassOnClasspath(String fullyQualifiedClassName) {
		return !isClassOnClasspath(fullyQualifiedClassName);
	}

	private boolean isClassOnClasspath(String fullyQualifiedClassName) {
		final boolean DO_NOT_INITIALIZE_CLASS = false;
		final ClassLoader THIS_TEST_CLASSLOADER = getThisTestClassLoader();
		try {
			Class.forName(fullyQualifiedClassName, DO_NOT_INITIALIZE_CLASS,	THIS_TEST_CLASSLOADER);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	private ClassLoader getThisTestClassLoader() {
		return this.getClass().getClassLoader();
	}

	private void checkAspectJWeaverExistsOnClasspath() {		
		if (isNotClassOnClasspath("org.aspectj.weaver.reflect.ReflectionWorld$ReflectionWorldException")) {
			fail("AspectJWeaver dependency is not safisfied.");
		}
	}

	private static MavenDependency maven(String groupId, String artifactId) {
		return new MavenDependency(groupId, artifactId);
	}

	private static String artifactId(String artifactId) {
		return artifactId;
	}
	
	private static String groupId(String groupId) {
		return groupId;
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

	private void givenLoggableAspectIsManagedBySpring() {
		springContext.register(LoggableAspect.class);
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

class MavenDependency {
	private String groupId;
	private String artifactId;
	
	public MavenDependency(String groupId, String artifactId) {
		this.groupId = groupId;
		this.artifactId = artifactId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((artifactId == null) ? 0 : artifactId.hashCode());
		result = prime * result
				+ ((groupId == null) ? 0 : groupId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MavenDependency other = (MavenDependency) obj;
		if (artifactId == null) {
			if (other.artifactId != null)
				return false;
		} else if (!artifactId.equals(other.artifactId))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		return true;
	}		
}