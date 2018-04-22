package com.eurodyn.qlack.fuse.settings.exception;


import com.eurodyn.qlack.common.exceptions.QException;

/**
 * Exception class for forum module
 *
 * @author European Dynamics SA.
 */
public class SettingsException extends QException {

  /**
   * @param message
   */
  public SettingsException(String message) {
    super(message);
  }

  public SettingsException(String message, Throwable cause) {
    super(message, cause);
  }
}
