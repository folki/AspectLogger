package sk.folki.aspectlogger;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

// TODO Extract interface
class LoggerGetter {

	@SuppressWarnings("rawtypes")
	public Logger getLoggerFor(ProceedingJoinPoint joinPoint) {
		Class classOfReachedJoinPoint = getClassOfJoinPoint(joinPoint);
		Logger log = getLoggerForClass(classOfReachedJoinPoint);
		return log;
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