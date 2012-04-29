package sk.folki.aspectlogger;

import org.aspectj.lang.ProceedingJoinPoint;

class LoggableProceedor {

	public ProcessingResult proceedToJoinPoint(ProceedingJoinPoint joinPoint) {
		try {
			Object returnedObject = joinPoint.proceed();
			return ProcessingResult.createOkResult(returnedObject);
		} catch (Throwable error) {
			return ProcessingResult.createErrorResult(error);
		}
	}
	
}