package com.eurodyn.qlack.fuse.imaging.exception;

import com.eurodyn.qlack.common.exceptions.QException;

/**
 * A generic exception for errors related to image processing.
 */
public class ImagingException extends QException {

  public ImagingException(String message) {
    super(message);
  }

  public ImagingException(String message, Throwable cause) {
    super(message, cause);
  }
}
