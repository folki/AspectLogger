package sk.folki.aspectlogger.core;

public class LoggableMethodInvocation {
	private	boolean isOk;
	private Throwable caughtException;
	private Object returnedObject;
	
	public LoggableMethodInvocation(Object returnedObject) {
		isOk = true;
		this.returnedObject = returnedObject;
	}
	
	public LoggableMethodInvocation(Throwable error) {
		isOk = false;
		caughtException = error;
	}

	public static LoggableMethodInvocation createOkResult(Object returnedObject) {
		return new LoggableMethodInvocation(returnedObject);
	}
	
	public static LoggableMethodInvocation createErrorResult(Throwable error) {
		return new LoggableMethodInvocation(error);
	}
	
	public Throwable getOccuredError() {
		return caughtException;
	}		

	public boolean isSuccessfull() {
		return isOk;
	}
	
	public boolean isErrorOccured() {
		return !isSuccessfull();
	}
	
	public Object getReturnedObject() {
		return returnedObject;
	}
}