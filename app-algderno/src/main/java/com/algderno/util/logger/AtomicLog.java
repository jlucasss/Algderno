package com.algderno.util.logger;

public class AtomicLog {
	
	private final String date;
	private final String message;

	public AtomicLog(String date, String message) {
		this.date = date;
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public String getMessage() {
		return message;
	}
	
}