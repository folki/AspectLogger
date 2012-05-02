package sk.folki.aspectlogger.client;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class LogAspect {
	private LoggableMethodProcessor loggableMethodProcessor;
	
	public LogAspect() {
		loggableMethodProcessor = new InvokeAndLogLoaggableMethod();
	}
	
	@Around(value = "@annotation(loggableAnnotation)", argNames = "loggableMethod, loggableAnnotation")
	public void loggableMethodHasBeenReached(ProceedingJoinPoint loggableMethod, Loggable loggableAnnotation) throws Throwable {
		invokedAndLogLoggableMethod(loggableMethod, loggableAnnotation);
	}
	
	private void invokedAndLogLoggableMethod(ProceedingJoinPoint loggableMethod, Loggable loggableAnnotation) throws Throwable {
		loggableMethodProcessor.processLoaggableMethod(loggableMethod, loggableAnnotation);
	}
}
