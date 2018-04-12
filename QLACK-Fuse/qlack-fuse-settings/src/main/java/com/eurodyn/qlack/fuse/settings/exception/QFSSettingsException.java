package com.eurodyn.qlack.fuse.settings.exception;


import com.eurodyn.qlack.common.exceptions.QCException;

/**
 * Exception class for forum module
 *
 * @author European Dynamics SA.
 */
public class QFSSettingsException extends QCException {

  /**
   * @param message
   */
  public QFSSettingsException(String message) {
    super(message);
  }

  public QFSSettingsException(String message, Throwable cause) {
    super(message, cause);
  }
}
