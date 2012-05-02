package sk.folki.aspectlogger.client;

import org.aspectj.lang.ProceedingJoinPoint;

import sk.folki.aspectlogger.core.LoggableMethodInvocation;

class DefaultLoggableMethodProceeder implements LoggableMethodProceeder {
	private LoggableMethodInvocationFactory loggableMethodInvocationFactory;

	public DefaultLoggableMethodProceeder() {
		this.loggableMethodInvocationFactory = new DefaultLoggableMethodInvocationFactory();
	}
	
	@Override
	public LoggableMethodInvocation proceedToJoinPoint(ProceedingJoinPoint loggableMethod) {
		try {
			Object returnedObject = loggableMethod.proceed();
			return loggableMethodInvocationFactory.createSuccessfullMethodInvocation(returnedObject);
		} catch (Throwable occuredError) {
			return loggableMethodInvocationFactory.createFailedMethodInvocation(occuredError);			
		}
	}
	
}