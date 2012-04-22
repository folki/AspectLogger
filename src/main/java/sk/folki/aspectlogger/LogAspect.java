package sk.folki.aspectlogger;

import static org.apache.log4j.Level.DEBUG;
import static org.apache.log4j.Level.INFO;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
				logOkDebugMessage(log, joinPoint, loggable);
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
	}

	private void logErrorDebugMessage(Logger log, ProceedingJoinPoint joinPoint, Loggable loggable, ProcessingResult processingResult) {
		String logMessage = assembleErrorDebugLogMessage(joinPoint, loggable);
		log.error(logMessage);
	}

	private String assembleErrorDebugLogMessage(ProceedingJoinPoint joinPoint, Loggable loggable) {
		return "ErrorDebugMessage";
	}

	private void logErrorInfoMessage(Logger log, Loggable loggable, ProcessingResult processingResult) {
		String logMessage = assembleErrorInfoLogMessage(loggable);
		log.error(logMessage);
	}

	private void logOkDebugMessage(Logger log, ProceedingJoinPoint joinPoint, Loggable loggable) {
		String logMessage = assembleOkDebugLogMessage(joinPoint, loggable);
		log.debug(logMessage);
	}

	private String assembleOkDebugLogMessage(ProceedingJoinPoint joinPoint, Loggable loggable) {
		return "OkDebugMessage";
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
		String infoMessage = loggable.value();
		return infoMessage;
	}

	private ProcessingResult proccedToLoggableMethod(ProceedingJoinPoint joinPoint) {		
		try {
			joinPoint.proceed();
			return ProcessingResult.createOkResult();
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
		
		public ProcessingResult() {
			isOk = true;
		}
		
		public ProcessingResult(Throwable error) {
			isOk = false;
			caughtException = error;
		}

		public static ProcessingResult createOkResult() {
			return new ProcessingResult();
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
	}
}
