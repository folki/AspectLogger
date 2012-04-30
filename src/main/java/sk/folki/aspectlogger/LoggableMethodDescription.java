package sk.folki.aspectlogger;


class LoggableMethodDescription {
	private Parameters parameters;
	private String logMessage;

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	
	public String getLogMessage() {
		return logMessage;
	}
	
	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

}
