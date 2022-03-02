package ar.edu.itba.paw.webapp.exceptionMapper;

import io.jsonwebtoken.JwtException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<JwtException> {
    @Override
    public Response toResponse(final JwtException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
    }
}
