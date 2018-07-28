package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception wrapper. This wrapper is also taken into account by
 * RestExceptionHandler in qlack-util-data in order to provide annotation-driven error messages to
 * your REST callers.
 *
 * Note that this wrappers hides the underlying exception resulting in loosing the stacktrace of
 * the underlying error, so make sure you properly log your original exception (this is taken
 * care automatically if you use the qlack-util-data ExceptionWrapper annotation in your
 * REST controllers).
 */
public class QExceptionWrapper extends QException {

  public QExceptionWrapper(String message) {
    super(message);
  }
}