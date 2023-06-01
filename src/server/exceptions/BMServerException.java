package server.exceptions;

import utility.enums.ErrorType;

public class BMServerException extends Exception{
	
	ErrorType errorType;
	
	public BMServerException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}
	
	public BMServerException(String message) {
		super(message);
	}
	
	public ErrorType getErrorType() {
		return errorType;
	}
}
