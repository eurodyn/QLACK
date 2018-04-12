package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception superclass to facilitate marking of
 * authentication-related exceptions.
 *
 */
public class QCSecurityAuthenticationException extends QCSecurityException {
	private static final long serialVersionUID = -7341692118839270522L;

	public QCSecurityAuthenticationException() {
		super();
	}

	public QCSecurityAuthenticationException(String msg) {
		super(msg);
	}
}
