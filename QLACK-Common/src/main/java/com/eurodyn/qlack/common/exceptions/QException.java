package com.eurodyn.qlack.common.exceptions;

import java.io.Serializable;

/**
 * A generic superclass to indicate any kind of runtime exception. This needs to remain a
 * RuntimeException in order for Aries to correctly rollback transactions.
 */
public abstract class QException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = 4808786528779863568L;

  protected QException() {
  }

  protected QException(String message) {
    super(message);
  }

  protected QException(String message, Throwable cause) {
    super(message, cause);
  }

  protected QException(Throwable cause) {
    super(cause);
  }

  protected QException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
