package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception superclass to facilitate marking of any type of
 * security-related exceptions.
 *
 */
public abstract class QCSecurityException extends QCException {
	private static final long serialVersionUID = -8412287217789350614L;
	
	public QCSecurityException() {
		super();
	}
	
	public QCSecurityException(String msg) {
		super(msg);
	}
}
