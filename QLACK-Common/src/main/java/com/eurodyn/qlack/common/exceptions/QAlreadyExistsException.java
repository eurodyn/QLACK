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
}
