package com.eurodyn.qlack.fuse.aaa.exception;

/**
 * The AuthorizationException is thrown when a request
 * is not authorized for the specific user
 * @author European Dynamics
 */
public class AuthorizationException extends AAAException {

        public AuthorizationException(String msg) {
            super(msg);
        }
    }
