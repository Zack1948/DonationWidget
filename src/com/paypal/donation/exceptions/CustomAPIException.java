package com.paypal.donation.exceptions;

public class CustomAPIException extends Exception {

	private static final long serialVersionUID = 1L;

	public CustomAPIException() {
	}

	public CustomAPIException(String message) {
		super(message);
	}

	public CustomAPIException(Throwable cause) {
		super(cause);
	}

	public CustomAPIException(String message, Throwable cause) {
		super(message, cause);
	}
	/*
	public CustomAPIException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	*/
}
