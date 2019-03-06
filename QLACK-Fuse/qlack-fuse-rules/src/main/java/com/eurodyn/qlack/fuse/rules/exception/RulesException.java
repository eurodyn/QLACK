package com.eurodyn.qlack.fuse.rules.exception;

import com.eurodyn.qlack.common.exceptions.QException;

/**
 * @author European Dynamics
 */
public class RulesException extends QException {

    private static final long serialVersionUID = 4852993089765648460L;

    public RulesException(String message) {
        super(message);
    }

    public RulesException(String message, Throwable cause) {
        super(message, cause);
    }

    public RulesException(Throwable cause) {
        super(cause);
    }

}

