package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception representing an error while trying to mutate data.
 */
public class QMutationNotPermittedException extends QException {

  public QMutationNotPermittedException() {
    super();
  }

  public QMutationNotPermittedException(String message) {
    super(message);
  }

  public QMutationNotPermittedException(String message, Throwable cause) {
    super(message, cause);
  }
}
