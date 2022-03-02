package ar.edu.itba.paw.webapp.exceptionMapper;

import ar.edu.itba.paw.model.exceptions.ConflictException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {
    @Override
    public Response toResponse(ConflictException e) {
        return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}
