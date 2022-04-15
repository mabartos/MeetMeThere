package org.mabartos.meetmethere.model.exception;

public class ModelNotFoundException extends Exception {

    public ModelNotFoundException(String message) {
        super(message);
    }

    public ModelNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
