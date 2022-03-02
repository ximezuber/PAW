package ar.edu.itba.paw.webapp.exceptionMapper;

import org.springframework.security.access.AccessDeniedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {
    @Override
    public Response toResponse(final AccessDeniedException e) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
