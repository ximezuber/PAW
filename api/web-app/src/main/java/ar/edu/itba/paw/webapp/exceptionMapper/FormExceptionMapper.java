package ar.edu.itba.paw.webapp.exceptionMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FormExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(prepareMessage(e))
                .type("text/plain")
                .build();
    }

    private String prepareMessage(ConstraintViolationException exception) {
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
//            String msg = messageSource.getMessage(cv.getMessage(), null, Locale.getDefault());
//            String connector = messageSource.getMessage("connector.invalid", null, Locale.getDefault());
//            message.append(cv.getInvalidValue().toString()).append(" ").append(connector).append(" ")
//                    .append(msg).append("\n");
        }
        return message.toString();
    }
}
