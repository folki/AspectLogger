package sk.folki.aspectlogger.core;

public interface AspectLogger {

	public void log(LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult);

}