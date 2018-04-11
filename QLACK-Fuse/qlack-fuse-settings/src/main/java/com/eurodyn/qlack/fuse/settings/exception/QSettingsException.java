package com.eurodyn.qlack.fuse.settings.exception;


import com.eurodyn.qlack.common.exceptions.QException;

/**
 * Exception class for forum module
 *
 * @author European Dynamics SA.
 */
public class QSettingsException extends QException {

  /**
   * @param message
   */
  public QSettingsException(String message) {
    super(message);
  }

  public QSettingsException(String message, Throwable cause) {
    super(message, cause);
  }
}
