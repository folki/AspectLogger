package sk.folki.aspectlogger.core;

public class DefaultAspectLogger implements AspectLogger {
	private LoggerGetter loggerGetter;
	private MessageGenerator logRecordGenerator;
	
	public DefaultAspectLogger() {
		loggerGetter = new LoggerGetter();
		logRecordGenerator = new MessageGenerator();
	}

	@Override
	public void log(LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		LoggableMethodLogger loggableMethodLogger = loggerGetter.getLoggerFor(loggableMethod);
		LoggableMethodLoggerConfig loggableMethodLoggerConfig  = loggableMethodLogger.getConfig();
		Message logRecord = logRecordGenerator.generateMessage(loggableMethodLoggerConfig, loggableMethod, processingResult);		
		loggableMethodLogger.write(logRecord);
	}
}