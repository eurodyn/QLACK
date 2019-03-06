package com.eurodyn.qlack.common.exception;

/**
 * An exception indicating that something went wrong while processing a signed
 * ticket.
 *
 */
public class QSecurityTicketException extends QSecurityException {
	private static final long serialVersionUID = -7341692118839270522L;

	public QSecurityTicketException() {
		super();
	}

	public QSecurityTicketException(String msg) {
		super(msg);
	}
}
