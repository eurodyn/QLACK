package com.eurodyn.qlack.util.data.exceptions;

/**
 * A generic error reply to be returned in wrapped exceptions where the original exception is
 * hidden. The message of this class is the logMessage of {@link ExceptionWrapper}.
 */
public class ErrorReplyDTO {
  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
