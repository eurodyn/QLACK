package com.eurodyn.qlack.common.exceptions;

/**
 * A generic exception representing a "can not save" condition.
 *
 * @author EUROPEAN DYNAMICS SA
 */
public class QCouldNotDeleteException extends QException {

    public QCouldNotDeleteException() {
        super();
    }

    public QCouldNotDeleteException(String message) {
        super(message);
    }

    public QCouldNotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

}
