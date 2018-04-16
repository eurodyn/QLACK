package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception representing a "does not exist" condition.
 */
public class QDoesNotExistException extends QException {

  public QDoesNotExistException() {
    super();
  }

  public QDoesNotExistException(String message) {
    super(message);
  }
}
