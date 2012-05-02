package sk.folki.aspectlogger.client;

import sk.folki.aspectlogger.core.LoggableMethodInvocation;

interface LoggableMethodInvocationFactory {

	public LoggableMethodInvocation createSuccessfullMethodInvocation(Object returnedObject);

	public LoggableMethodInvocation createFailedMethodInvocation(Throwable occuredError);
	
}