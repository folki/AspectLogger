package sk.folki.aspectlogger.core;

import static org.apache.log4j.Level.DEBUG;
import static org.apache.log4j.Level.ERROR;
import static org.apache.log4j.Level.INFO;
import static org.apache.log4j.Level.OFF;
import static org.apache.log4j.Level.TRACE;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

class Log4jLoggableMethodLogger implements LoggableMethodLogger {
	private Logger log4jLogger;
	private LoggableMethodLoggerConfig loggableMethodLoggerConfig;

	public Log4jLoggableMethodLogger(Logger logger) {
		this.log4jLogger = logger;
		this.loggableMethodLoggerConfig = generateConfig(logger);
	}

	private LoggableMethodLoggerConfig generateConfig(Logger logger) {
		return new LoggableMethodLoggerConfig(logger);
	}

	@Override
	public LoggableMethodLoggerConfig getConfig() {
		return loggableMethodLoggerConfig;
	}

	@Override
	public void write(Message logRecord) {
		Level logRecordLevel = getLogRecordLevel(logRecord);
		writeToLog(logRecordLevel, logRecord);
	}
	
	private Level getLogRecordLevel(Message logRecord) {		
		if (logRecord.isFailureMessage()) {
			return getFailureLogRecordLevel(logRecord);
		} else {
			return getSuccessLogRecordLevel(logRecord);
		}
	}

	private Level getSuccessLogRecordLevel(Message logRecord) {
		Level configuredLogLevel = loggableMethodLoggerConfig.getLevel();
		Level logRecordLevel = OFF;
		if (configuredLogLevel == TRACE) {
			logRecordLevel = TRACE;				
		} else if (configuredLogLevel == DEBUG) {
			logRecordLevel = DEBUG;
		} else if (configuredLogLevel == INFO) {
			logRecordLevel = INFO;
		} else {
			logRecordLevel = OFF;
		}
		return logRecordLevel;
	}

	private Level getFailureLogRecordLevel(Message logRecord) {
		return ERROR;
	}

	/**
	 * 
	 * @param level passing the OFF level causes that log record wont be written to log file.  
	 * @param logMessage
	 */
	private void writeToLog(Level level, Message logMessage) {
		if (level == OFF) {
			return;
		}
		log4jLogger.log(level, logMessage);
	}
}