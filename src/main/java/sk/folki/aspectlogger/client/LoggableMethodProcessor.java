package sk.folki.aspectlogger.client;

import org.aspectj.lang.ProceedingJoinPoint;

interface LoggableMethodProcessor {

	void processLoaggableMethod(ProceedingJoinPoint loggableMethod, Loggable loggableAnnotation) throws Throwable;
	
}