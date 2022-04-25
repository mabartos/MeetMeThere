package org.mabartos.meetmethere.interaction.rest.api;

import com.fasterxml.jackson.core.JsonParseException;
import org.jboss.resteasy.spi.Failure;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    private static int getStatusCode(Throwable throwable) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        if (throwable instanceof WebApplicationException) {
            WebApplicationException ex = (WebApplicationException) throwable;
            status = ex.getResponse().getStatus();
        }

        if (throwable instanceof Failure) {
            Failure f = (Failure) throwable;
            status = f.getErrorCode();
        }

        if (throwable instanceof JsonParseException) {
            status = Response.Status.BAD_REQUEST.getStatusCode();
        }

        if (throwable instanceof ModelNotFoundException) {
            status = Response.Status.NOT_FOUND.getStatusCode();
        }

        if (throwable instanceof ModelDuplicateException) {
            status = Response.Status.CONFLICT.getStatusCode();
        }

        return status;
    }
}