package sk.folki.aspectlogger.core;

import static org.apache.log4j.Level.DEBUG;
import static org.apache.log4j.Level.ERROR;
import static org.apache.log4j.Level.INFO;
import static org.apache.log4j.Level.OFF;
import static org.apache.log4j.Level.TRACE;
import static org.apache.log4j.Level.WARN;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


// TODO Refactor
class LoggableMethodLogger {
	private Logger log4jLogger;
	private LoggableMethodLoggerConfig config;

	public LoggableMethodLogger(Logger logger) {
		this.log4jLogger = logger;
		this.config = generateConfig(logger);
	}

	private LoggableMethodLoggerConfig generateConfig(Logger logger) {
		return new LoggableMethodLoggerConfig(logger);
	}

	public LoggableMethodLoggerConfig getConfig() {
		return config;
	}

	public Level getLevel() {
		return log4jLogger.getLevel();
	}

	public void write(Message logMessage) {
		Level logLevel = log4jLogger.getLevel();
		if (logMessage.isInfoMessage()) {
			if (logLevel == TRACE) {
				logMessage(log4jLogger, TRACE, logMessage);
			} else if (logLevel == DEBUG) {
				logMessage(log4jLogger, DEBUG, logMessage);
			} else if (logLevel == INFO) {
				logMessage(log4jLogger, INFO, logMessage);
			} else if (logLevel == WARN) {
				doNothing();
			} else if (logLevel == ERROR) {
				doNothing();
			} else if (logLevel == OFF) {
				doNothing();
			}
		} else {
			if (logLevel == TRACE) {
				logMessage(log4jLogger, ERROR, logMessage);
			} else if (logLevel == DEBUG) {
				logMessage(log4jLogger, ERROR, logMessage);
			} else if (logLevel == INFO) {
				logMessage(log4jLogger, ERROR, logMessage);
			} else if (logLevel == WARN) {
				logMessage(log4jLogger, ERROR, logMessage);
			} else if (logLevel == ERROR) {
				logMessage(log4jLogger, ERROR, logMessage);
			} else if (logLevel == OFF) {
				doNothing();
			}
		}
	}
	
	private void logMessage(Logger logger, Level level, Message logMessage) {
		logger.log(level, logMessage);
	}
	
	private void doNothing() {	
	}
}