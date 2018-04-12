package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception representing an "already exists" condition.
 */
public class QCAlreadyExistsException extends QCException {

  public QCAlreadyExistsException() {
    super();
  }

  public QCAlreadyExistsException(String message) {
    super(message);
  }
}
