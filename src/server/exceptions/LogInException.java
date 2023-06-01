package server.exceptions;

import utility.enums.ErrorType;

public class LogInException extends Exception{
	
	ErrorType errorType;
	
	public LogInException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}
	
	public LogInException(String message) {
		super(message);
	}
	
	public ErrorType getErrorType() {
		return errorType;
	}

}
