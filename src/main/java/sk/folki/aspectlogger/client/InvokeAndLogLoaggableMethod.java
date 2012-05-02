package sk.folki.aspectlogger.client;

import org.aspectj.lang.ProceedingJoinPoint;

import sk.folki.aspectlogger.core.AspectLogger;
import sk.folki.aspectlogger.core.LoggableMethodDescription;
import sk.folki.aspectlogger.core.LoggableMethodInvocation;

class InvokeAndLogLoaggableMethod implements LoggableMethodProcessor {
	private LoggableMethodProceeder loggableProceeder; 
	private AspectLogger aspectLogger;
	
	public InvokeAndLogLoaggableMethod() {
		loggableProceeder = new DefaultLoggableMethodProceeder();
		aspectLogger = new AspectLogger();
	}
	
	@Override
	public void processLoaggableMethod(ProceedingJoinPoint loggableMethod, Loggable loggableAnnotation) throws Throwable {
		invokedAndLogLoggableMethod(loggableMethod, loggableAnnotation);		
	}
	
	private void invokedAndLogLoggableMethod(ProceedingJoinPoint loggableMethod, Loggable loggableAnnotation) throws Throwable {
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