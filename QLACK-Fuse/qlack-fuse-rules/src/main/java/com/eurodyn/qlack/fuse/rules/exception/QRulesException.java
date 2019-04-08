package com.eurodyn.qlack.fuse.rules.exception;

import com.eurodyn.qlack.common.exception.QException;

public class QRulesException extends QException {

    private static final long serialVersionUID = 4852993089765648460L;

    public QRulesException(String message) {
        super(message);
    }

    public QRulesException(String message, Throwable cause) {
        super(message, cause);
    }

    public QRulesException(Throwable cause) {
        super(cause);
    }

}
