package com.paypal.donation.exceptions;

public class CustomUIException extends Exception {

	private static final long serialVersionUID = 1L;

	public CustomUIException() {
		super();
	}

	/*
	public CustomUIException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}*/

	public CustomUIException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomUIException(String message) {
		super(message);
	}

	public CustomUIException(Throwable cause) {
		super(cause);
	}

}
