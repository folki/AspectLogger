package sk.folki.aspectlogger;

class ProcessingResult {
	private	boolean isOk;
	private Throwable caughtException;
	private Object returnedObject;
	
	public ProcessingResult(Object returnedObject) {
		isOk = true;
		this.returnedObject = returnedObject;
	}
	
	public ProcessingResult(Throwable error) {
		isOk = false;
		caughtException = error;
	}

	public static ProcessingResult createOkResult(Object returnedObject) {
		return new ProcessingResult(returnedObject);
	}
	
	public static ProcessingResult createErrorResult(Throwable error) {
		return new ProcessingResult(error);
	}
	
	Throwable getCaughtException() {
		return caughtException;
	}		

	public boolean isOk() {
		return isOk;
	}
	
	public Object getReturnedObject() {
		return returnedObject;
	}
}