package sk.folki.aspectlogger.core;

public class LoggableMethodInvocation {
	private	boolean isOk;
	private Throwable caughtException;
	private Object returnedObject;
		
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

	public void setSuccessfullyInvoked(boolean isSuccessfullyInvoked) {
		this.isOk = isSuccessfullyInvoked;
	}

	public void setReturnedObject(Object returnedObject) {
		this.returnedObject = returnedObject;
	}

	public void setOccuredError(Throwable occuredError) {
		this.caughtException = occuredError;
	}
}