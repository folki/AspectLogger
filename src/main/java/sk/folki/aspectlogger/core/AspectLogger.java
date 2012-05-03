package sk.folki.aspectlogger.core;

import static org.apache.log4j.Level.TRACE;
import static org.apache.log4j.Level.DEBUG;
import static org.apache.log4j.Level.INFO;
import static org.apache.log4j.Level.WARN;
import static org.apache.log4j.Level.ERROR;
import static org.apache.log4j.Level.OFF;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


//TODO Extract interface
public class AspectLogger {
	private LoggerGetter loggerGetter = new LoggerGetter();

	public void log(LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		// TODO Separate generating log message from writing log message
		
		Logger logger = loggerGetter.getLoggerFor(loggableMethod);
		Level logLevel = logger.getLevel();
		// Generate message
		String logMessage = null;
		if (processingResult.isSuccessfull()) {
			if (logLevel == INFO) {
				logMessage = generateOkInfoMessage(loggableMethod);
			} else if (logLevel == DEBUG) {
				logMessage = generateOkDebugMessage(loggableMethod, processingResult);
			} 			
		} else {
			if (logLevel == INFO) {
				logMessage = generateErrorInfoMessage(loggableMethod, processingResult);
			} else if (logLevel == DEBUG) {
				logMessage = generateErrorDebugMessage(loggableMethod, processingResult);
			}				
		}
		// Log generated message
		if (processingResult.isSuccessfull()) {
			if (logLevel == TRACE) {
				logMessage(logger, TRACE, logMessage);
			} else if (logLevel == DEBUG) {
				logMessage(logger, DEBUG, logMessage);
			} else if (logLevel == INFO) {
				logMessage(logger, INFO, logMessage);
			} else if (logLevel == WARN) {
				doNothing();
			} else if (logLevel == ERROR) {
				doNothing();
			} else if (logLevel == OFF) {
				doNothing();
			}
		} else {
			if (logLevel == TRACE) {
				logMessage(logger, ERROR, logMessage);
			} else if (logLevel == DEBUG) {
				logMessage(logger, ERROR, logMessage);
			} else if (logLevel == INFO) {
				logMessage(logger, ERROR, logMessage);
			} else if (logLevel == WARN) {
				logMessage(logger, ERROR, logMessage);
			} else if (logLevel == ERROR) {
				logMessage(logger, ERROR, logMessage);
			} else if (logLevel == OFF) {
				doNothing();
			}
		}
	}	
	
	private void logMessage(Logger logger, Level level, String messageToLog) {
		logger.log(level, messageToLog);
	}

	private void doNothing() {	
	}

	private String generateOkInfoMessage(LoggableMethodDescription loggableMethod) {
		String logMessage = assembleOkInfoLogMessage(loggableMethod);
		return logMessage;
	}
	
	private String assembleOkInfoLogMessage(LoggableMethodDescription loggableMethod) {
		String infoMessage = getLoggableOperationMessage(loggableMethod);
		return infoMessage;
	}
	
	private String getLoggableOperationMessage(LoggableMethodDescription loggableMethod) {
		return loggableMethod.getLogMessage();
	}
	
	private String generateOkDebugMessage(LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		Parameters loggableOperationParameters = loggableMethod.getParameters();
		String logMessage = assembleOkDebugLogMessage(loggableOperationParameters, loggableMethod, processingResult);
		return logMessage;
	}

	private String assembleOkDebugLogMessage(Parameters loggableOperationParameters, LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		// Service operation WITH PARAMS 0=Parameter type instance RETURNED Return type instance
		String loggableOperationMessage = getLoggableOperationMessage(loggableMethod);
		String loggableOperationParametersStringChain = convertParametersToStringChain(loggableOperationParameters);
		String loggableOperationReturnedValue = getValueReturnedFromProcessedLoggableMethod(processingResult);
		String errorDebugLogMessage = 
			loggableOperationMessage + " WITH PARAMS " + loggableOperationParametersStringChain + " RETURNED " + loggableOperationReturnedValue;
		return errorDebugLogMessage;
	}
	
	private String convertParametersToStringChain(Parameters loggableOperationParameters) {
		String parametersStringChain = "";
		for (String parameterName: loggableOperationParameters.getNames()) {
			Object parameterValue = loggableOperationParameters.getValue(parameterName);
			parametersStringChain += parameterName + "=" + parameterValue + ", ";
		}
		parametersStringChain = parametersStringChain.substring(0, parametersStringChain.length() - 2);
		return parametersStringChain;
	}
	
	private String getValueReturnedFromProcessedLoggableMethod(LoggableMethodInvocation processingResult) {
		return processingResult.getReturnedObject().toString();
	}
	
	private String generateErrorInfoMessage(LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		String logMessage = assembleErrorInfoLogMessage(loggableMethod);
		return logMessage;
	}

	private String assembleErrorInfoLogMessage(LoggableMethodDescription loggableMethod) {
		String infoMessage = loggableMethod.getLogMessage();
		String errorMessage = "Error: " + infoMessage;
		return errorMessage;
	}
	
	private String generateErrorDebugMessage(LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		Parameters loggableOperationParameters = loggableMethod.getParameters();
		String logMessage = assembleErrorDebugLogMessage(loggableOperationParameters, loggableMethod, processingResult);
		return logMessage;
	}

	private String assembleErrorDebugLogMessage(Parameters loggableOperationParameters, LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		// Service operation WITH PARAMS parameter=Parameter type instance THROWS Error message.
		String loggableOperationMessage = getLoggableOperationMessage(loggableMethod);
		String loggableOperationParametersStringChain = convertParametersToStringChain(loggableOperationParameters);
		String loggableOperationOccuredError = getErrorOccuredDurringProcessingLoggableMethod(processingResult);
		String errorDebugLogMessage = 
			loggableOperationMessage + " WITH PARAMS " + loggableOperationParametersStringChain + " THROWN " + loggableOperationOccuredError ;
		return errorDebugLogMessage;
	}
	
	private String getErrorOccuredDurringProcessingLoggableMethod(LoggableMethodInvocation processingResult) {
		return processingResult.getOccuredError().getMessage();
	}
}