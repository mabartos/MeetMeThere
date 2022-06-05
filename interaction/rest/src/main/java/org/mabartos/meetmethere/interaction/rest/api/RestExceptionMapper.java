package org.mabartos.meetmethere.interaction.rest.api;

import com.fasterxml.jackson.core.JsonParseException;
import io.quarkus.cache.CacheException;
import org.jboss.resteasy.spi.Failure;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.model.exception.ModelNotFoundException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class RestExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger log = Logger.getLogger(RestExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        return Response.status(getStatusCode(exception)).entity(exception.getMessage()).build();
    }

    private static int getStatusCode(Throwable throwable) {
        final int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        if (throwable instanceof CacheException) {
            throwable = throwable.getCause();
        }

        if (throwable instanceof SecurityException) {
            return Response.Status.FORBIDDEN.getStatusCode();
        }

        if (throwable instanceof WebApplicationException) {
            WebApplicationException ex = (WebApplicationException) throwable;
            return ex.getResponse().getStatus();
        }

        if (throwable instanceof Failure) {
            Failure f = (Failure) throwable;
            return f.getErrorCode();
        }

        if (throwable instanceof JsonParseException || throwable instanceof UnsupportedOperationException) {
            return Response.Status.BAD_REQUEST.getStatusCode();
        }

        if (throwable instanceof ModelNotFoundException) {
            return Response.Status.NOT_FOUND.getStatusCode();
        }

        if (throwable instanceof ModelDuplicateException) {
            return Response.Status.CONFLICT.getStatusCode();
        }

        if (status == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            throwable.printStackTrace();
        }

        return status;
    }
}
