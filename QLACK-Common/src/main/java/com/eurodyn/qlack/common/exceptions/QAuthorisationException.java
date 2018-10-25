package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception superclass to facilitate marking of authorisation-related exceptions.
 */
public class QAuthorisationException extends QSecurityException {

  private static final long serialVersionUID = 3887709297788547031L;

  public QAuthorisationException() {
    super();
  }

  public QAuthorisationException(String msg) {
    super(msg);
  }

  public QAuthorisationException(String msg, Object... args) {
    super(msg, args);
  }
}
