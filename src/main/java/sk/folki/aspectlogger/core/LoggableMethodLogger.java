package sk.folki.aspectlogger.core;

interface LoggableMethodLogger {

	public LoggableMethodLoggerConfig getConfig();

	public void write(Message logMessage);

}