package sk.folki.aspectlogger.client;

import org.aspectj.lang.ProceedingJoinPoint;

import sk.folki.aspectlogger.core.LoggableMethodDescription;
import sk.folki.aspectlogger.core.Parameters;

class ProceedingJoinPointAdapter {
	private ProceedingJoinPoint joinPoint;
	private Loggable loggable;

	private ProceedingJoinPointAdapter(ProceedingJoinPoint joinPoint, Loggable loggable) {
		this.joinPoint = joinPoint;
		this.loggable = loggable; 
	}

	public static ProceedingJoinPointAdapter adapt(ProceedingJoinPoint joinPoint, Loggable loggable) {
		return new ProceedingJoinPointAdapter(joinPoint, loggable);
	}

	public LoggableMethodDescription toLoggableMethod() {
		Parameters parameters = getParameters(joinPoint);
		String logMessage = getLogMessage(loggable);
		Class<?> parentClass = getParentClass(joinPoint);
		LoggableMethodDescription loggableMethod = new LoggableMethodDescription();
		loggableMethod.setParameters(parameters);
		loggableMethod.setLogMessage(logMessage);
		loggableMethod.setParentClass(parentClass);
		return loggableMethod;
	}
	
	private Class<?> getParentClass(ProceedingJoinPoint joinPoint) {
		return joinPoint.getTarget().getClass();
	}

	private Parameters getParameters(ProceedingJoinPoint joinPoint) {
		Parameters parameters = new Parameters();
		final Object[] parameterValues = joinPoint.getArgs();
        for (int i = 0; i < parameterValues.length; i++) {
        	String name = String.valueOf(i);
        	Object value = parameterValues[i];
        	parameters.addParameter(name, value);
        }
	    return parameters;
	}
	
	private String getLogMessage(Loggable loggable) {
		return loggable.value();
	}
}
