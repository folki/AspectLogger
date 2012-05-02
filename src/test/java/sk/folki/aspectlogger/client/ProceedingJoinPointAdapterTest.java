package sk.folki.aspectlogger.client;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.mock.Mock;

import sk.folki.aspectlogger.client.Loggable;
import sk.folki.aspectlogger.client.ProceedingJoinPointAdapter;
import sk.folki.aspectlogger.core.LoggableMethodDescription;

public class ProceedingJoinPointAdapterTest extends UnitilsJUnit4 {
	private Mock<ProceedingJoinPoint> joinPoint;
	private Mock<Loggable> loggable;
	
	private LoggableMethodDescription loggableMethod;
	
	@Before
	public void init() throws Exception {
		setUpJointPoint();
		setUpLoggable();
	}
	
	private void setUpJointPoint() throws Exception {
		loggableMethodHasNoParameters();
		loggableMethodBelongsToClass(WhateverClass.class);
	}

	private class WhateverClass {		
	}

	private void loggableMethodHasNoParameters() {
		loggableMethodHasParameters(noParameters());
	}

	private Object[] noParameters() {
		return new Object[] {};
	}

	private void setUpLoggable() {
		loggableMethodMessageIs("Default message");
	}

	@Test
	public void testLogMessageValue() {
		loggableMethodMessageIs("Service operation");
		adapterAdaptsLoggableAnnotatedMethodToLoggableMethodObject();
		loggableMethodCreatedByAdapterHasLogMessage("Service operation");
	}
	
	private void loggableMethodMessageIs(String loggableValue) {
		loggable.resetBehavior();
		loggable.returns(loggableValue).value();
	}

	private void adapterAdaptsLoggableAnnotatedMethodToLoggableMethodObject() {
		loggableMethod = 
			ProceedingJoinPointAdapter.adapt(joinPoint.getMock(), loggable.getMock()).toLoggableMethod();
	}

	private void loggableMethodCreatedByAdapterHasLogMessage(String expectedLogMessage) {
		assertThat(loggableMethod.getLogMessage(), equalTo(expectedLogMessage));
	}

	@Test
	public void testParametersValue() {
		loggableMethodHasParameters(
				methodParameter(1),
				methodParameter("test"),
				methodParameter(objectWithToStringMethod("object parameter"))
		);
		adapterAdaptsLoggableAnnotatedMethodToLoggableMethodObject();
		loggableMethodCreatedByAdapterHasParameters(
				parameter(name("0"), value("1")),
				parameter(name("1"), value("test")),
				parameter(name("2"), value("object parameter"))
		);
	}

	private void loggableMethodHasParameters(Object... methodParameters) {
		joinPoint.returns(methodParameters).getArgs();
	}

	private Object methodParameter(Object methodParameter) {
		return methodParameter;
	}

	private Object objectWithToStringMethod(String toStringMethodReturnValue) {
		return new ToStringMethodOverridenType(toStringMethodReturnValue);
	}
	
	private class ToStringMethodOverridenType {
		private String toStringReturnValued;

		public ToStringMethodOverridenType(String toStringMethodReturnValue) {
			this.toStringReturnValued = toStringMethodReturnValue;
		}

		public String toString() {
			return toStringReturnValued;
		}
		
	}

	private void loggableMethodCreatedByAdapterHasParameters(ExpectedLoggableMethodParameter... parameters) {
		for (ExpectedLoggableMethodParameter parameter: parameters) {
			loggableMethodCreatedByAdapterHasParameter(parameter);
		}
	}

	private void loggableMethodCreatedByAdapterHasParameter(ExpectedLoggableMethodParameter parameter) {
		String nameOfExpectedParameter = parameter.getName();
		String valueOfExpectedParameter = parameter.getValue();
		assertThatLoggableMethodCreatedByAdapterContainsParameterWithValue(
				nameOfExpectedParameter, valueOfExpectedParameter);
	}

	private void assertThatLoggableMethodCreatedByAdapterContainsParameterWithValue(
			String nameOfExpectedParameter, String valueOfExpectedParameter) {
		assertThatLoggableMethodCreatedByAdapterContainsParameter(nameOfExpectedParameter);
		String realValueOfExpectedParameter = getRealValueOfParameterOfLoggableMethodCreatedByAdapter(nameOfExpectedParameter);
		assertThat(realValueOfExpectedParameter, equalTo(valueOfExpectedParameter));
	}

	private String getRealValueOfParameterOfLoggableMethodCreatedByAdapter(String parameterName) {
		return loggableMethod.getParameters().getValue(parameterName).toString();
	}

	private void assertThatLoggableMethodCreatedByAdapterContainsParameter(String nameOfExpectedParameter) {
		String[] allParameterNames = loggableMethod.getParameters().getNames();
		if (arrayHasNotItem(allParameterNames, nameOfExpectedParameter)) {
			throw new AssertionError("Parameter with name '" + nameOfExpectedParameter + "' was not found.");
		}
	}

	private boolean arrayHasNotItem(Object[] array, Object item) {
		return !arrayHasItem(array, item);
	}

	private boolean arrayHasItem(Object[] array, Object item) {
		for (Object currentItem: array) {
			if (currentItem.equals(item)) {
				return true;
			}
		}
		return false;
	}

	private ExpectedLoggableMethodParameter parameter(String name, String value) {
		return new ExpectedLoggableMethodParameter(name, value);
	}
	
	private class ExpectedLoggableMethodParameter {
		private String name;
		private String value;

		public ExpectedLoggableMethodParameter(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
	}

	private String name(String name) {
		return name;
	}

	private String value(String value) {
		return value;
	}
	
	@Ignore	// I was not able to find a way how to implement this test. There is a issue with mocking the jointPoint where mocked instance always returns java.lang.Class instead of concrete class name setUp during preparing the test.
	@Test
	public void testParentClassValue() throws Exception {
		loggableMethodBelongsToClass(SomeClass.class);
		adapterAdaptsLoggableAnnotatedMethodToLoggableMethodObject();
		loggableMethodCreatedByAdapterHasParentClassValue(SomeClass.class);
	}

	private <T> void loggableMethodBelongsToClass(Class<T> targetClass) throws Exception {
		joinPoint.resetBehavior();
		joinPoint.returns(targetClass).getTarget().getClass();
	}

	private void loggableMethodCreatedByAdapterHasParentClassValue(Class<?> expectedParentClass) {
		assertThat(loggableMethod.getParentClass().getName(), equalTo(expectedParentClass.getName()));
	}

	private class SomeClass {
	}
}
