package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception representing an "already exists" condition.
 */
public class QAlreadyExistsException extends QException {

  public QAlreadyExistsException() {
    super();
  }

  public QAlreadyExistsException(String message) {
    super(message);
  }

  public QAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public QAlreadyExistsException(String message, Object... args) {
    super(message, args);
  }
}
