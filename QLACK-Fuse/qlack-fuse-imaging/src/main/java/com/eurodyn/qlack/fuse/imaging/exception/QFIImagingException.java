package com.eurodyn.qlack.fuse.imaging.exception;

import com.eurodyn.qlack.common.exceptions.QCException;

/**
 * A generic exception for errors related to image processing.
 */
public class QFIImagingException extends QCException {

  public QFIImagingException(String message) {
    super(message);
  }

  public QFIImagingException(String message, Throwable cause) {
    super(message, cause);
  }
}
