package sk.folki.aspectlogger;

import org.aspectj.lang.ProceedingJoinPoint;

//TODO Extract interface
class LoggableProceedor {

	public LoggableMethodInvocation proceedToJoinPoint(ProceedingJoinPoint joinPoint) {
		try {
			Object returnedObject = joinPoint.proceed();
			return LoggableMethodInvocation.createOkResult(returnedObject);
		} catch (Throwable error) {
			return LoggableMethodInvocation.createErrorResult(error);
		}
	}
	
}