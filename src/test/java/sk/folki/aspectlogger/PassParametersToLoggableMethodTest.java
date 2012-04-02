package sk.folki.aspectlogger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PassParametersToLoggableMethodTest extends AbstractEndToEndTest {
	private Service service;
	
	public PassParametersToLoggableMethodTest() {
		service = getBean(Service.class);
	}
	
	@Override
	protected Class<?>[] getServiceBeansConfig() {
		return new Class[] {ServiceImpl.class};
	}
	
	@Test
	public void testPassParametersToLoggableMethodWhenIsProcceed() {
		final String parameter = "passedParameterValue";
		whenOperationWithParameterIsInvoked(parameter);
		thenParameterIsPassedToTheMethodByLogAspectWhenProceedingJointPoint(parameter);
	}
	
	private void thenParameterIsPassedToTheMethodByLogAspectWhenProceedingJointPoint(String expectedParameterValue) {
		String parameterPassedToOperation = service.getPassedParameter();
		assertThat(parameterPassedToOperation, equalTo(expectedParameterValue));
	}

	private void whenOperationWithParameterIsInvoked(String operationParameter) {
		service.operation(operationParameter);
	}

	static interface Service {
		void operation(String stringParameter);
		String getPassedParameter();
	}
	
	static class ServiceImpl implements Service {
		private String passedParameter;
		
		@Override
		@Loggable("A operation with parameter.")
		public void operation(String stringParameter) {
			this.passedParameter = stringParameter;
		}
		
		public String getPassedParameter() {
			return passedParameter;
		}
	}


}
