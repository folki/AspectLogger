package sk.folki.aspectlogger;

import static org.apache.log4j.Level.DEBUG;
import static org.apache.log4j.Level.INFO;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


class AspectLogger {

	public void log(Logger logger, LoggableMethod loggableMethod, ProcessingResult processingResult) {
		Level logLevel = logger.getLevel();
		if (processingResult.isOk()) {
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
	
	private void logOkInfoMessage(Logger log, LoggableMethod loggableMethod) {
		String logMessage = assembleOkInfoLogMessage(loggableMethod);
		log.info(logMessage);
	}
	
	private String assembleOkInfoLogMessage(LoggableMethod loggableMethod) {
		String infoMessage = getLoggableOperationMessage(loggableMethod);
		return infoMessage;
	}
	
	private String getLoggableOperationMessage(LoggableMethod loggableMethod) {
		return loggableMethod.getLogMessage();
	}
	
	private void logOkDebugMessage(Logger log, LoggableMethod loggableMethod, ProcessingResult processingResult) {
		Parameters loggableOperationParameters = loggableMethod.getParameters();
		String logMessage = assembleOkDebugLogMessage(loggableOperationParameters, loggableMethod, processingResult);
		log.debug(logMessage);
	}

	private String assembleOkDebugLogMessage(Parameters loggableOperationParameters, LoggableMethod loggableMethod, ProcessingResult processingResult) {
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
	
	private String getValueReturnedFromProcessedLoggableMethod(ProcessingResult processingResult) {
		return processingResult.getReturnedObject().toString();
	}
	
	private void logErrorInfoMessage(Logger log, LoggableMethod loggableMethod, ProcessingResult processingResult) {
		String logMessage = assembleErrorInfoLogMessage(loggableMethod);
		log.error(logMessage);
	}

	private String assembleErrorInfoLogMessage(LoggableMethod loggableMethod) {
		String infoMessage = loggableMethod.getLogMessage();
		String errorMessage = "Error: " + infoMessage;
		return errorMessage;
	}
	
	private void logErrorDebugMessage(Logger log, LoggableMethod loggableMethod, ProcessingResult processingResult) {
		Parameters loggableOperationParameters = loggableMethod.getParameters();
		String logMessage = assembleErrorDebugLogMessage(loggableOperationParameters, loggableMethod, processingResult);
		log.error(logMessage);
	}

	private String assembleErrorDebugLogMessage(Parameters loggableOperationParameters, LoggableMethod loggableMethod, ProcessingResult processingResult) {
		// Service operation WITH PARAMS parameter=Parameter type instance THROWS Error message.
		String loggableOperationMessage = getLoggableOperationMessage(loggableMethod);
		String loggableOperationParametersStringChain = convertParametersToStringChain(loggableOperationParameters);
		String loggableOperationOccuredError = getErrorOccuredDurringProcessingLoggableMethod(processingResult);
		String errorDebugLogMessage = 
			loggableOperationMessage + " WITH PARAMS " + loggableOperationParametersStringChain + " THROWN " + loggableOperationOccuredError ;
		return errorDebugLogMessage;
	}
	
	private String getErrorOccuredDurringProcessingLoggableMethod(ProcessingResult processingResult) {
		return processingResult.getCaughtException().getMessage();
	}
}