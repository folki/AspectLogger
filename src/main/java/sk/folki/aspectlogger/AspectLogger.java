package sk.folki.aspectlogger;

import static org.apache.log4j.Level.DEBUG;
import static org.apache.log4j.Level.INFO;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

//TODO Extract interface
class AspectLogger {
	private LoggerGetter loggerGetter = new LoggerGetter();

	public void log(LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		Logger logger = loggerGetter.getLoggerFor(loggableMethod);
		Level logLevel = logger.getLevel();
		if (processingResult.isSuccessfull()) {
			if (logLevel == INFO) {
				logOkInfoMessage(logger, loggableMethod);
			} else if (logLevel == DEBUG) {
				logOkDebugMessage(logger, loggableMethod, processingResult);
			} 			
		} else {
			if (logLevel == INFO) {
				logErrorInfoMessage(logger, loggableMethod, processingResult);
			} else if (logLevel == DEBUG) {
				logErrorDebugMessage(logger, loggableMethod, processingResult);
			}				
		}
	}	
	
	private void logOkInfoMessage(Logger log, LoggableMethodDescription loggableMethod) {
		String logMessage = assembleOkInfoLogMessage(loggableMethod);
		log.info(logMessage);
	}
	
	private String assembleOkInfoLogMessage(LoggableMethodDescription loggableMethod) {
		String infoMessage = getLoggableOperationMessage(loggableMethod);
		return infoMessage;
	}
	
	private String getLoggableOperationMessage(LoggableMethodDescription loggableMethod) {
		return loggableMethod.getLogMessage();
	}
	
	private void logOkDebugMessage(Logger log, LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		Parameters loggableOperationParameters = loggableMethod.getParameters();
		String logMessage = assembleOkDebugLogMessage(loggableOperationParameters, loggableMethod, processingResult);
		log.debug(logMessage);
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
	
	private void logErrorInfoMessage(Logger log, LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		String logMessage = assembleErrorInfoLogMessage(loggableMethod);
		log.error(logMessage);
	}

	private String assembleErrorInfoLogMessage(LoggableMethodDescription loggableMethod) {
		String infoMessage = loggableMethod.getLogMessage();
		String errorMessage = "Error: " + infoMessage;
		return errorMessage;
	}
	
	private void logErrorDebugMessage(Logger log, LoggableMethodDescription loggableMethod, LoggableMethodInvocation processingResult) {
		Parameters loggableOperationParameters = loggableMethod.getParameters();
		String logMessage = assembleErrorDebugLogMessage(loggableOperationParameters, loggableMethod, processingResult);
		log.error(logMessage);
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