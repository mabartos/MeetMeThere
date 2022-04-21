package org.mabartos.meetmethere.api.model.exception;

public class ModelDuplicateException extends Exception {

    public ModelDuplicateException(String message) {
        super(message);
    }

    public ModelDuplicateException(String message, Throwable t) {
        super(message, t);
    }
}
