package sk.folki.aspectlogger.core;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;



// TODO Extract interface
class LoggerGetter {

	@Deprecated
	@SuppressWarnings("rawtypes")
	public Logger getLoggerFor(ProceedingJoinPoint joinPoint) {
		Class classOfReachedJoinPoint = getClassOfJoinPoint(joinPoint);
		Logger log = getLoggerForClass(classOfReachedJoinPoint);
		return log;
	}		
	
	public LoggableMethodLogger getLoggerFor(LoggableMethodDescription loggableMethod) {
		String loggerName = getLoggerNameFor(loggableMethod);
		Logger logger = getLogger(loggerName);
		return new LoggableMethodLogger(logger);
	}
	
	private Logger getLogger(String loggerName) {
		return Logger.getLogger(loggerName);
	}

	private String getLoggerNameFor(LoggableMethodDescription loggableMethod) {
		return loggableMethod.getParentClass().getName();
	}

	@SuppressWarnings("rawtypes")
	private Class getClassOfJoinPoint(JoinPoint joinPoint) {
		return joinPoint.getTarget().getClass();
	}
	
	@SuppressWarnings("rawtypes")
	private Logger getLoggerForClass(Class clazz) {
		return Logger.getLogger(clazz);
	}
}