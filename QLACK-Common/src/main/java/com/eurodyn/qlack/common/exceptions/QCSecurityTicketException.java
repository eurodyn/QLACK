package com.eurodyn.qlack.common.exceptions;

/**
 * An exception indicating that something went wrong while processing a signed
 * ticket.
 *
 */
public class QCSecurityTicketException extends QCSecurityException {
	private static final long serialVersionUID = -7341692118839270522L;

	public QCSecurityTicketException() {
		super();
	}

	public QCSecurityTicketException(String msg) {
		super(msg);
	}
}
