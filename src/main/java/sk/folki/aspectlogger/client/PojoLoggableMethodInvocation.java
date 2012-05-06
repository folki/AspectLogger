package sk.folki.aspectlogger.client;

import sk.folki.aspectlogger.core.LoggableMethodInvocation;

class PojoLoggableMethodInvocation implements LoggableMethodInvocation {
	private	boolean isOk;
	private Throwable caughtException;
	private Object returnedObject;
		
	@Override
	public Throwable getOccuredError() {
		return caughtException;
	}		

	@Override
	public boolean isSuccessfull() {
		return isOk;
	}
	
	@Override
	public boolean isErrorOccured() {
		return !isSuccessfull();
	}
	
	@Override
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