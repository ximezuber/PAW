package ar.edu.itba.paw.webapp.exceptionMapper;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;

@Provider
public class NotFoundExceptionMapper2 implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException e) {
       return Response.temporaryRedirect(URI.create("/index.html")).build();
    }
}
