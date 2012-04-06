package sk.folki.aspectlogger;

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
		String logMessage = null;
		if (processingResult.isOk()) {
			logMessage = assembleInfoLogMessage(loggable);
			log.info(logMessage);
		} else {
			logMessage = assembleErrorLogMessage(loggable);
			log.error(logMessage);
			Throwable caughtException = processingResult.getCaughtException();
			throw caughtException;
		}
	}

	private String assembleErrorLogMessage(Loggable loggable) {
		String infoMessage = loggable.value();
		String errorMessage = "Error: " + infoMessage;
		return errorMessage;
	}

	private String assembleInfoLogMessage(Loggable loggable) {
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
