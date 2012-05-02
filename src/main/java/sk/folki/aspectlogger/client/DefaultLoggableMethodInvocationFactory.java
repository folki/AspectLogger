package sk.folki.aspectlogger.client;

import sk.folki.aspectlogger.core.LoggableMethodInvocation;

class DefaultLoggableMethodInvocationFactory implements LoggableMethodInvocationFactory {

	public LoggableMethodInvocation createSuccessfullMethodInvocation(Object returnedObject) {
		LoggableMethodInvocation successfullMethodInvocation = new LoggableMethodInvocation();
		successfullMethodInvocation.setSuccessfullyInvoked(true);
		successfullMethodInvocation.setReturnedObject(returnedObject);
		return successfullMethodInvocation;
	}

	public LoggableMethodInvocation createFailedMethodInvocation(Throwable occuredError) {
		LoggableMethodInvocation failedMethodInvocation = new LoggableMethodInvocation();
		failedMethodInvocation.setSuccessfullyInvoked(false);
		failedMethodInvocation.setOccuredError(occuredError);
		return failedMethodInvocation;			
	}		
}