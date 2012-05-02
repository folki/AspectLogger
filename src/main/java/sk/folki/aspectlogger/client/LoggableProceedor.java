package sk.folki.aspectlogger.client;

import org.aspectj.lang.ProceedingJoinPoint;

import sk.folki.aspectlogger.core.LoggableMethodInvocation;

//TODO Extract interface
public class LoggableProceedor {

	public LoggableMethodInvocation proceedToJoinPoint(ProceedingJoinPoint joinPoint) {
		try {
			Object returnedObject = joinPoint.proceed();
			return LoggableMethodInvocation.createOkResult(returnedObject);
		} catch (Throwable error) {
			return LoggableMethodInvocation.createErrorResult(error);
		}
	}
	
}