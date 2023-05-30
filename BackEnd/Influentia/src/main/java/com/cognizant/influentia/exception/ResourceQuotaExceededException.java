package com.cognizant.influentia.exception;

public class ResourceQuotaExceededException extends Exception {

	private static final long serialVersionUID = 1L;

	public ResourceQuotaExceededException(String message) {
		super(message);
	}
}