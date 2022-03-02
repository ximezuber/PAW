package ar.edu.itba.paw.webapp.exceptionMapper;

import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        LoggerFactory.getLogger(ExceptionMapper.class).error("A generic exception was been thrown", e);
        Response.StatusType type = getStatusType(e);
        return Response
                .status(type.getStatusCode())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    private Response.StatusType getStatusType(Exception e) {
        if (e instanceof WebApplicationException) {
            return ((WebApplicationException) e).getResponse().getStatusInfo();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}
