package sk.folki.aspectlogger.client;

import sk.folki.aspectlogger.core.LoggableMethodInvocation;

class DefaultLoggableMethodInvocationFactory implements LoggableMethodInvocationFactory {

	public LoggableMethodInvocation createSuccessfullMethodInvocation(Object returnedObject) {
		PojoLoggableMethodInvocation successfullMethodInvocation = new PojoLoggableMethodInvocation();
		successfullMethodInvocation.setSuccessfullyInvoked(true);
		successfullMethodInvocation.setReturnedObject(returnedObject);
		return successfullMethodInvocation;
	}

	public LoggableMethodInvocation createFailedMethodInvocation(Throwable occuredError) {
		PojoLoggableMethodInvocation failedMethodInvocation = new PojoLoggableMethodInvocation();
		failedMethodInvocation.setSuccessfullyInvoked(false);
		failedMethodInvocation.setOccuredError(occuredError);
		return failedMethodInvocation;			
	}		
}