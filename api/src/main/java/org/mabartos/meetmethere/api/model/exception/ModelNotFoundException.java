package org.mabartos.meetmethere.api.model.exception;

public class ModelNotFoundException extends Exception {

    public ModelNotFoundException() {
        super();
    }

    public ModelNotFoundException(String message) {
        super(message);
    }

    public ModelNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
