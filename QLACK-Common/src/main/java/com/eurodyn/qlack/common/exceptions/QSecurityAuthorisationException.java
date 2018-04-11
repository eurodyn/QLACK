package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception superclass to facilitate marking of
 * authorisation-related exceptions.
 *
 */
public class QSecurityAuthorisationException extends QSecurityException {
	private static final long serialVersionUID = 3887709297788547031L;

	public QSecurityAuthorisationException() {
		super();
	}

	public QSecurityAuthorisationException(String msg) {
		super(msg);
	}
}
