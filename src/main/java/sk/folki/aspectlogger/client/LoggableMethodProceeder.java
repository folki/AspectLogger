package sk.folki.aspectlogger.client;

import org.aspectj.lang.ProceedingJoinPoint;

import sk.folki.aspectlogger.core.LoggableMethodInvocation;

interface LoggableMethodProceeder {

	public LoggableMethodInvocation proceedToJoinPoint(ProceedingJoinPoint loggableMethod);

}