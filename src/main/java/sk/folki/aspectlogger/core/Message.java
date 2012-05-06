package sk.folki.aspectlogger.core;

// Refactor
class Message {
	private String messageLevel;
	private String logMessage;

	@Deprecated
	public Message(String messageLevel, String logMessage) {			
		this.messageLevel = messageLevel;
		this.logMessage = logMessage;
	}

	public boolean isSuccessMessage() {
		return messageLevel.equals("info");
	}	
	
	public String toString() {
		return logMessage;
	}

	public boolean isFailureMessage() {
		return !isSuccessMessage();
	}
}