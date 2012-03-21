package sk.folki.aspectlogger;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
	
	@Before(value = "@annotation(loggable)", argNames = "joinPoint, loggable")
	@SuppressWarnings("rawtypes")
	public void pointcutAdvice(JoinPoint joinPoint, Loggable loggable) {		
		Class classOfReachedJoinPoint = getClassOfJoinPoint(joinPoint);
		Logger log = getLoggerForClass(classOfReachedJoinPoint);
		String message = loggable.value();
		log.info(message);
		
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
