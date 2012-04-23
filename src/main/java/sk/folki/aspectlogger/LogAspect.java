package sk.folki.aspectlogger;

import static org.apache.log4j.Level.DEBUG;
import static org.apache.log4j.Level.INFO;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
	
	@Around(value = "@annotation(loggable)", argNames = "joinPoint, loggable")
	@SuppressWarnings("rawtypes")
	public void pointcutAdvice(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {		
		Class classOfReachedJoinPoint = getClassOfJoinPoint(joinPoint);
		Logger log = getLoggerForClass(classOfReachedJoinPoint);
		ProcessingResult processingResult = proccedToLoggableMethod(joinPoint);
		Level logLevel = log.getLevel();
		if (processingResult.isOk()) {
			if (logLevel == INFO) {
				logOkInfoMessage(log, loggable);
			} else if (logLevel == DEBUG) {
				logOkDebugMessage(log, joinPoint, loggable, processingResult);
			} 			
		} else {
			if (logLevel == INFO) {
				logErrorInfoMessage(log, loggable, processingResult);
			} else if (logLevel == DEBUG) {
				logErrorDebugMessage(log, joinPoint, loggable, processingResult);
			}
			Throwable caughtException = processingResult.getCaughtException();
			throw caughtException;
		}
		
		// --
		/*logLevel = logLevelDecisionMaker.getLogLevel
		messageToLog = messageCreator.createMessage(joinPoint, loggable, processingResult);
		aspectLogger.logMessage(logger, logLevel, messageToLog);*/
	}

	private void logErrorDebugMessage(Logger log, ProceedingJoinPoint joinPoint, Loggable loggable, ProcessingResult processingResult) {
		String logMessage = assembleErrorDebugLogMessage(joinPoint, loggable, processingResult);
		log.error(logMessage);
	}

	private String assembleErrorDebugLogMessage(ProceedingJoinPoint joinPoint, Loggable loggable, ProcessingResult processingResult) {
		// Service operation WITH PARAMS parameter=Parameter type instance THROWS Error message.
		String loggableOperationMessage = getLoggableOperationMessage(loggable);
		Parameters loggableOperationParameters = getParameters(joinPoint);
		String loggableOperationParametersStringChain = convertParametersToStringChain(loggableOperationParameters);
		String loggableOperationOccuredError = getErrorOccuredDurringProcessingLoggableMethod(processingResult);
		String errorDebugLogMessage = 
			loggableOperationMessage + " WITH PARAMS " + loggableOperationParametersStringChain + " THROWN " + loggableOperationOccuredError ;
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

	private String getErrorOccuredDurringProcessingLoggableMethod(ProcessingResult processingResult) {
		return processingResult.getCaughtException().getMessage();
	}

	private Parameters getParameters(ProceedingJoinPoint joinPoint) {
		Parameters parameters = new Parameters();
		final Signature signature = joinPoint.getStaticPart().getSignature();
	    if(signature instanceof MethodSignature){
	        final MethodSignature ms = (MethodSignature) signature;
	        final String[] parameterNames = ms.getParameterNames();
	        final Object[] parameterValues = joinPoint.getArgs();
	        for (int i = 0; i < parameterValues.length; i++) {
	        	String name;
	        	if (parameterNames == null || parameterNames[i] == null) {
					name = String.valueOf(i);
	        	} else {
	        		name = parameterNames[i];
	        	}
	        	Object value = parameterValues[i];
	        	parameters.addParameter(name, value);
	        }
	    }
	    return parameters;
	}

	private static class Parameters {
		private Map<String, Object> parameters = new LinkedHashMap<String, Object>();

		public void addParameter(String name, Object value) {
			parameters.put(name, value);
		}

		public String[] getNames() {
			return parameters.keySet().toArray(new String[parameters.size()]);
		}
		
		public Object getValue(String parameterName) {
			return parameters.get(parameterName);
		}
	}

	private void logErrorInfoMessage(Logger log, Loggable loggable, ProcessingResult processingResult) {
		String logMessage = assembleErrorInfoLogMessage(loggable);
		log.error(logMessage);
	}

	private void logOkDebugMessage(Logger log, ProceedingJoinPoint joinPoint, Loggable loggable, ProcessingResult processingResult) {
		String logMessage = assembleOkDebugLogMessage(joinPoint, loggable, processingResult);
		log.debug(logMessage);
	}

	private String assembleOkDebugLogMessage(ProceedingJoinPoint joinPoint, Loggable loggable, ProcessingResult processingResult) {
		// Service operation WITH PARAMS 0=Parameter type instance RETURNED Return type instance
		String loggableOperationMessage = getLoggableOperationMessage(loggable);
		Parameters loggableOperationParameters = getParameters(joinPoint);
		String loggableOperationParametersStringChain = convertParametersToStringChain(loggableOperationParameters);
		String loggableOperationReturnedValue = getValueReturnedFromProcessedLoggableMethod(processingResult);
		String errorDebugLogMessage = 
			loggableOperationMessage + " WITH PARAMS " + loggableOperationParametersStringChain + " RETURNED " + loggableOperationReturnedValue;
		return errorDebugLogMessage;
	}

	private String getValueReturnedFromProcessedLoggableMethod(ProcessingResult processingResult) {
		return processingResult.getReturnedObject().toString();
	}

	private void logOkInfoMessage(Logger log, Loggable loggable) {
		String logMessage = assembleOkInfoLogMessage(loggable);
		log.info(logMessage);
	}

	private String assembleErrorInfoLogMessage(Loggable loggable) {
		String infoMessage = loggable.value();
		String errorMessage = "Error: " + infoMessage;
		return errorMessage;
	}

	private String assembleOkInfoLogMessage(Loggable loggable) {
		String infoMessage = getLoggableOperationMessage(loggable);
		return infoMessage;
	}

	private String getLoggableOperationMessage(Loggable loggable) {
		return loggable.value();
	}

	private ProcessingResult proccedToLoggableMethod(ProceedingJoinPoint joinPoint) {		
		try {
			Object returnedObject = joinPoint.proceed();
			return ProcessingResult.createOkResult(returnedObject);
		} catch (Throwable error) {
			return ProcessingResult.createErrorResult(error);
		}
	}

	@SuppressWarnings("rawtypes")
	private Logger getLoggerForClass(Class clazz) {
		return Logger.getLogger(clazz);
	}

	@SuppressWarnings("rawtypes")
	private Class getClassOfJoinPoint(JoinPoint joinPoint) {
		return joinPoint.getTarget().getClass();
	}
	
	private static class ProcessingResult {
		private	boolean isOk;
		private Throwable caughtException;
		private Object returnedObject;
		
		public ProcessingResult(Object returnedObject) {
			isOk = true;
			this.returnedObject = returnedObject;
		}
		
		public ProcessingResult(Throwable error) {
			isOk = false;
			caughtException = error;
		}

		public static ProcessingResult createOkResult(Object returnedObject) {
			return new ProcessingResult(returnedObject);
		}
		
		public static ProcessingResult createErrorResult(Throwable error) {
			return new ProcessingResult(error);
		}
		
		Throwable getCaughtException() {
			return caughtException;
		}		

		public boolean isOk() {
			return isOk;
		}
		
		public Object getReturnedObject() {
			return returnedObject;
		}
	}
}
