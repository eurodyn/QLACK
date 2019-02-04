package com.eurodyn.qlack.fuse.audit.exceptions;

import com.eurodyn.qlack.common.exceptions.QException;

/**
 * @author European Dynamics
 */
public class AlreadyExistsException extends QException {
    public AlreadyExistsException(String msg) {
        super(msg);
    }
}
