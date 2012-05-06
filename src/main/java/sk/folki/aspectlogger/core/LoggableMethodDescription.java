package sk.folki.aspectlogger.core;

public interface LoggableMethodDescription {

	public String getInfoMessage();

	public Parameters getParameters();

	public Class<?> getParentClass();
	
}