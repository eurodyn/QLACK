package com.eurodyn.qlack.common.exception;

/**
 * A generic exception representing a "can not delete" condition.
 */
public class QCouldNotSaveException extends QException {

  public QCouldNotSaveException() {
    super();
  }

  public QCouldNotSaveException(String message) {
    super(message);
  }

  public QCouldNotSaveException(String message, Throwable cause) {
    super(message, cause);
  }
}
