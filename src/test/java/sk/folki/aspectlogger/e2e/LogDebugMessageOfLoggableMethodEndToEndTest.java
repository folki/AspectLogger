package sk.folki.aspectlogger.e2e;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import sk.folki.aspectlogger.client.Loggable;

public class LogDebugMessageOfLoggableMethodEndToEndTest extends AbstractEndToEndTest {
	private final static String MESSAGE = "Service operation";
	private Service service;
	private Logger log;
	
	private ServiceOperationParameter serviceOperationParameter;
	private Throwable serviceOperationException;
	private ServiceOperationReturnValue serviceOperationReturnedValue;

	public LogDebugMessageOfLoggableMethodEndToEndTest() {
		service = getBean(Service.class);
		log = getLoggerWithDefaultConfiguration(ServiceImpl.class);
	}
	
	@Before
	public void init() {
		givenServiceLoggerIsSetToLevel(Level.DEBUG);
		givenServiceLoggerAppenderUsesSimpleLayout();
	}
	
	private void givenServiceLoggerIsSetToLevel(Level logLevel) {
		setLoggerToLogLevel(log, logLevel);
	}
	
	private void givenServiceLoggerAppenderUsesSimpleLayout() {
		setSimpleLayoutAppenderToLogger(log);
	}
	
	@Test
	public void testLogDebugMessageOfLoggableMethodWithParameterAndReturnType() throws Throwable {	
		givenServiceMethodDefinesLoggableMessageAs("Service operation");
		givenToStringMethodOfParameterTypeReturns("Parameter type instance");
		givenToStringMethodOfReturnTypeReturns("Return type instance");
		whenServiceMethodIsInvoked();
		thenLogShouldContainTheRecord("DEBUG - Service operation WITH PARAMS 0=Parameter type instance RETURNED Return type instance\n");
	}

	private void givenServiceMethodDefinesLoggableMessageAs(String expectedMessage) {
		// Loggable annotation value should be set to the value that is passed to this method
		assertThat(MESSAGE, equalTo(expectedMessage));
	}
	
	private void givenToStringMethodOfParameterTypeReturns(String toStringMessageOfParameterType) {
		serviceOperationParameter = new ServiceOperationParameter(toStringMessageOfParameterType);
	}
	
	private void givenToStringMethodOfReturnTypeReturns(String toStringMessageOfReturnType) {
		serviceOperationReturnedValue = new ServiceOperationReturnValue(toStringMessageOfReturnType);
		service.setOperationReturnsValue(serviceOperationReturnedValue);
	}
	
	private void whenServiceMethodIsInvoked() throws Throwable {
		try {
			service.operation(serviceOperationParameter);
		} catch (Exception e) {
			// Testing the re-throwing caught exception is outside of scope of this tests.
		}
	}

	private void thenLogShouldContainTheRecord(String expectedLogRecord) {
		StringBuffer logMessages = getLogMessages();
		assertThat(logMessages.toString(), equalTo(expectedLogRecord));
	}
	
	@Test
	public void testLogDebugMessageOfLoggableMethodWithParameterWhenThrowsException() throws Throwable {		
		givenServiceMethodDefinesLoggableMessageAs("Service operation");
		givenToStringMethodOfParameterTypeReturns("Parameter type instance");
		givenToStringMethodOfReturnTypeReturns("Return type instance");
		givenServiceMethodThrowsException(new RuntimeException("Error message"));
		whenServiceMethodIsInvoked();
		thenLogShouldContainTheRecord("ERROR - Service operation WITH PARAMS 0=Parameter type instance THROWN Error message\n");
	}
	
	private void givenServiceMethodThrowsException(Exception exceptionToThrow) {
		serviceOperationException = exceptionToThrow;
		service.setExceptionIsThrownDuringOperationInvocation(serviceOperationException);
	}

	@Override
	protected Class<?>[] getServiceBeansConfig() {
		return new Class[] {ServiceImpl.class};
	}
	
	static interface Service {
		
		ServiceOperationReturnValue operation(ServiceOperationParameter parameter) throws Throwable;

		void setExceptionIsThrownDuringOperationInvocation (Throwable serviceOperationException);

		void setOperationReturnsValue(ServiceOperationReturnValue serviceOperationReturnedValue);
		
	}
	
	static class ServiceOperationReturnValue {
		private String toStringMessage;

		public ServiceOperationReturnValue(String toStringMessage) {
			this.toStringMessage = toStringMessage;
		}
		
		@Override
		public String toString() {
			return toStringMessage;
		}
	}
	
	static class ServiceOperationParameter {
		private String toStringMessage;

		public ServiceOperationParameter(String toStringMessage) {
			this.toStringMessage = toStringMessage;
		}
		
		@Override
		public String toString() {
			return toStringMessage;
		}
	}
	
	static class ServiceImpl implements Service {
		private ServiceOperationReturnValue valueToReturnFromOperation;
		private Throwable exceptionToThrowFromOperation;

		@Override
		@Loggable(MESSAGE)
		public ServiceOperationReturnValue operation(ServiceOperationParameter parameter) throws Throwable {
			if (shouldBeThrownException()) {
				throw exceptionToThrowFromOperation;
			} else {
				return valueToReturnFromOperation;
			}
		}

		private boolean shouldBeThrownException() {
			if (exceptionToThrowFromOperation == null) {
				return false;
			} else {
				return true;
			}
		}

		@Override
		public void setExceptionIsThrownDuringOperationInvocation(Throwable exceptionToThrow) {
			exceptionToThrowFromOperation = exceptionToThrow;
		}

		@Override
		public void setOperationReturnsValue(ServiceOperationReturnValue valueToReturn) {
			valueToReturnFromOperation = valueToReturn;
		}
		
	}

}
