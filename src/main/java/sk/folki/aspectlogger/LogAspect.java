package sk.folki.aspectlogger;



import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
	private LoggerGetter loggerGetter = new LoggerGetter();
	private LoggableProceedor loggableProceeder = new LoggableProceedor(); 
	private AspectLogger aspectLogger = new AspectLogger();
	
	@Around(value = "@annotation(loggable)", argNames = "joinPoint, loggable")
	public void pointcutAdvice(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
		Logger joinPointLogger = getLoggerFor(joinPoint);		
		LoggableMethod loggableMethod = getLoggableMethod(joinPoint, loggable);
		ProcessingResult processingResult = proccedToLoggableMethod(joinPoint);
		aspectLogger.log(joinPointLogger, loggableMethod, processingResult);
		if (!processingResult.isOk()) {
			Throwable caughtException = processingResult.getCaughtException();
			throw caughtException;
		}
	}
	
	private Logger getLoggerFor(ProceedingJoinPoint joinPoint) {
		return loggerGetter.getLoggerFor(joinPoint);		
	}
	
	private LoggableMethod getLoggableMethod(ProceedingJoinPoint joinPoint, Loggable loggable) {
		return ProceedingJoinPointAdapter.adapt(joinPoint, loggable).toLoggableMethod();
	}
	
	private ProcessingResult proccedToLoggableMethod(ProceedingJoinPoint joinPoint) {
		return loggableProceeder.proceedToJoinPoint(joinPoint);		
	}
}
