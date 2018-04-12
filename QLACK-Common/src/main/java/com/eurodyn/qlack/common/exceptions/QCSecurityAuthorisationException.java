package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception superclass to facilitate marking of
 * authorisation-related exceptions.
 *
 */
public class QCSecurityAuthorisationException extends QCSecurityException {
	private static final long serialVersionUID = 3887709297788547031L;

	public QCSecurityAuthorisationException() {
		super();
	}

	public QCSecurityAuthorisationException(String msg) {
		super(msg);
	}
}
