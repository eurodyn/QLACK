package com.eurodyn.qlack.fuse.search.exception;

import com.eurodyn.qlack.common.exceptions.QCException;

/**
 * A generic wrapping-exception for this module.
 */
public class QFESSearchException extends QCException {

  /**
   * Default empty constructor.
   */
  public QFESSearchException() {
    super();
  }

  /**
   * A constructor with a specific message.
   *
   * @param msg The message to include for this exception.
   */
  public QFESSearchException(String msg) {
    super(msg);
  }

  /**
   * A constructor with a specific message and an underlying exception cause
   * (root exception).
   *
   * @param msg The message to include for this exception.
   * @param e The root exception for this exception.
   */
  public QFESSearchException(String msg, Throwable e) {
    super(msg, e);
  }
}
