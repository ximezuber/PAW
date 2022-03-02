package ar.edu.itba.paw.webapp.exceptionMapper;

import ar.edu.itba.paw.model.exceptions.UnsupportedMediaTypeException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnsupportedMediaTypeExceptionMapper implements ExceptionMapper<UnsupportedMediaTypeException> {
    @Override
    public Response toResponse(UnsupportedMediaTypeException e) {
        return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).entity(e.getMessage()).build();
    }
}
