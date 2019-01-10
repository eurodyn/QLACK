package com.eurodyn.qlack.common.exceptions;

/**
 * A security exception representing an invalid nonce value.
 *
 * @author EUROPEAN DYNAMICS SA
 */
public class QInvalidNonceException extends QException {

    public QInvalidNonceException() {
    }

    public QInvalidNonceException(String message) {
        super(message);
    }

    public QInvalidNonceException(String message, Throwable cause) {
        super(message, cause);
    }

}
