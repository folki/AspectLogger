package sk.folki.aspectlogger.core;



//TODO Extract interface
public class AspectLogger {
	private LoggerGetter loggerGetter = new LoggerGetter();
	private MessageGenerator messageGenerator = new MessageGenerator();

	public void log(LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		LoggableMethodLogger loggableMethodLogger = loggerGetter.getLoggerFor(loggableMethod);
		LoggableMethodLoggerConfig loggableMethodLoggerConfig  = loggableMethodLogger.getConfig();
		Message logMessage = messageGenerator.generateMessage(loggableMethodLoggerConfig, loggableMethod, processingResult);		
		loggableMethodLogger.write(logMessage);
	}
}