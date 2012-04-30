package sk.folki.aspectlogger;

import org.aspectj.lang.ProceedingJoinPoint;


class ProceedingJoinPointAdapter {
	private ProceedingJoinPoint joinPoint;
	private Loggable loggable;

	private ProceedingJoinPointAdapter(ProceedingJoinPoint joinPoint, Loggable loggable) {
		this.joinPoint = joinPoint;
		this.loggable = loggable;
	}

	public static ProceedingJoinPointAdapter adapt(ProceedingJoinPoint joinPoint, Loggable loggable) {
		return new ProceedingJoinPointAdapter(joinPoint, loggable);
	}

	public LoggableMethodDescription toLoggableMethod() {
		Parameters parameters = getParameters(joinPoint);
		String logMessage = getLogMessage(loggable);
		LoggableMethodDescription loggableMethod = new LoggableMethodDescription();
		loggableMethod.setParameters(parameters);
		loggableMethod.setLogMessage(logMessage);
		return loggableMethod;
	}

	/*private Parameters getParameters(ProceedingJoinPoint joinPoint) {
		Parameters parameters = new Parameters();
		final Signature signature = joinPoint.getStaticPart().getSignature();
	    if(signature instanceof MethodSignature){
	        final MethodSignature ms = (MethodSignature) signature;
	        final String[] parameterNames = ms.getParameterNames();
	        final Object[] parameterValues = joinPoint.getArgs();
	        for (int i = 0; i < parameterValues.length; i++) {
	        	String name;
	        	if (parameterNames == null || parameterNames[i] == null) {
					name = String.valueOf(i);
	        	} else {
	        		name = parameterNames[i];
	        	}
	        	Object value = parameterValues[i];
	        	parameters.addParameter(name, value);
	        }
	    }
	    return parameters;
	}*/
	
	private Parameters getParameters(ProceedingJoinPoint joinPoint) {
		Parameters parameters = new Parameters();
		final Object[] parameterValues = joinPoint.getArgs();
        for (int i = 0; i < parameterValues.length; i++) {
        	String name = String.valueOf(i);
        	Object value = parameterValues[i];
        	parameters.addParameter(name, value);
        }
	    return parameters;
	}
	
	private String getLogMessage(Loggable loggable) {
		return loggable.value();
	}

}
