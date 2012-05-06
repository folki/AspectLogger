package sk.folki.aspectlogger.core;

public interface LoggableMethodDescription {

	public String getLogMessage();

	public Parameters getParameters();

	public Class<?> getParentClass();
	
}