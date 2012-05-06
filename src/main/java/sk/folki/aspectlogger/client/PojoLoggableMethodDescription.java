package sk.folki.aspectlogger.client;

import sk.folki.aspectlogger.core.LoggableMethodDescription;
import sk.folki.aspectlogger.core.Parameters;

class PojoLoggableMethodDescription implements LoggableMethodDescription {
	private Parameters parameters;
	private String logMessage;
	private Class<?> parentClass;

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public void setParentClass(Class<?> parentClass) {
		this.parentClass = parentClass;
	}
	
	@Override
	public String getLogMessage() {
		return logMessage;
	}
	
	@Override
	public Parameters getParameters() {
		return parameters;
	}
	
	@Override
	public Class<?> getParentClass() {
		return parentClass;
	}

}
