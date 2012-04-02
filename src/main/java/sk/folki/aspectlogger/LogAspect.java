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
	public void pointcutAdvice(ProceedingJoinPoint joinPoint, Loggable loggable) {		
		Class classOfReachedJoinPoint = getClassOfJoinPoint(joinPoint);
		Logger log = getLoggerForClass(classOfReachedJoinPoint);
		boolean loggableMethodFinishedNormally = proccedToLoggableMethod(joinPoint);
		String logMessage = null;
		if (loggableMethodFinishedNormally) {
			logMessage = assembleInfoLogMessage(loggable);
			log.info(logMessage);
		} else {
			logMessage = assembleErrorLogMessage(loggable);
			log.error(logMessage);
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

	private boolean proccedToLoggableMethod(ProceedingJoinPoint joinPoint) {		
		try {
			joinPoint.proceed();
			return true;
		} catch (Throwable e) {
			return false;
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
}
