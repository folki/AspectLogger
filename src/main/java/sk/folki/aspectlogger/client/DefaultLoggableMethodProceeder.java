package sk.folki.aspectlogger.client;

import org.aspectj.lang.ProceedingJoinPoint;

import sk.folki.aspectlogger.core.LoggableMethodInvocation;

class DefaultLoggableMethodProceeder implements LoggableMethodProceeder {

	@Override
	public LoggableMethodInvocation proceedToJoinPoint(ProceedingJoinPoint loggableMethod) {
		try {
			Object returnedObject = loggableMethod.proceed();
			return LoggableMethodInvocation.createOkResult(returnedObject);
		} catch (Throwable error) {
			return LoggableMethodInvocation.createErrorResult(error);
		}
	}
	
}