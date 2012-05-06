package sk.folki.aspectlogger.core;

public interface LoggableMethodInvocation {

	public Throwable getOccuredError();

	public boolean isSuccessfull();

	public boolean isErrorOccured();

	public Object getReturnedObject();

}