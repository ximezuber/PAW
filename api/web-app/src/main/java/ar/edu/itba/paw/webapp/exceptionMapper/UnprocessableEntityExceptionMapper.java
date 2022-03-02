package ar.edu.itba.paw.webapp.exceptionMapper;

import ar.edu.itba.paw.model.exceptions.UnprocessableEntityException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnprocessableEntityExceptionMapper implements ExceptionMapper<UnprocessableEntityException> {
    @Override
    public Response toResponse(UnprocessableEntityException e) {
        return Response.status(422).entity(e.getMessage()).build();
    }
}
