package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception representing a "does not exist" condition.
 */
public class QCDoesNotExistException extends QCException {

  public QCDoesNotExistException() {
    super();
  }

  public QCDoesNotExistException(String message) {
    super(message);
  }
}
