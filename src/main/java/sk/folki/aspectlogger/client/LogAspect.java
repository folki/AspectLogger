package sk.folki.aspectlogger.client;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import sk.folki.aspectlogger.core.AspectLogger;
import sk.folki.aspectlogger.core.LoggableMethodDescription;
import sk.folki.aspectlogger.core.LoggableMethodInvocation;

@Component
@Aspect
public class LogAspect {
	private LoggableProceedor loggableProceeder; 
	private AspectLogger aspectLogger;
	
	public LogAspect() {
		loggableProceeder = new LoggableProceedor();
		aspectLogger = new AspectLogger();
	}
	
	@Around(value = "@annotation(loggableAnnotation)", argNames = "loggableMethod, loggableAnnotation")
	public void invokedAndLogLoggableMethod(ProceedingJoinPoint loggableMethod, Loggable loggableAnnotation) throws Throwable {		
		LoggableMethodDescription loggableMethodDescription = createLoggableMethodDescription(loggableMethod, loggableAnnotation);
		LoggableMethodInvocation loggableMethodInvocation = proccedToLoggableMethod(loggableMethod);
		aspectLogger.log(loggableMethodDescription, loggableMethodInvocation);
		if (loggableMethodInvocation.isErrorOccured()) {
			Throwable occuredError = loggableMethodInvocation.getOccuredError();
			throw occuredError;
		}
	}
	
	private LoggableMethodDescription createLoggableMethodDescription(ProceedingJoinPoint loggableMethod, Loggable loggableAnnotation) {
		return ProceedingJoinPointAdapter.adapt(loggableMethod, loggableAnnotation).toLoggableMethod();
	}
	
	private LoggableMethodInvocation proccedToLoggableMethod(ProceedingJoinPoint loggableMethod) {
		return loggableProceeder.proceedToJoinPoint(loggableMethod);		
	}
}
