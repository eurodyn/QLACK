package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception representing a "can not delete" condition.
 *
 * @author EUROPEAN DYNAMICS SA
 */
public class QCouldNotSaveException extends QException {

    public QCouldNotSaveException() {
        super();
    }

    public QCouldNotSaveException(String message) {
        super(message);
    }

    public QCouldNotSaveException(String message, Throwable cause) {
        super(message, cause);
    }

}
