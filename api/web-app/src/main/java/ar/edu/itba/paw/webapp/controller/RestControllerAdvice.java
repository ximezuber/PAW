package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.ErrorMessage;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;

@ControllerAdvice(basePackages = {"ar.edu.itba.paw.webapp.controller"})
public class RestControllerAdvice {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(value = { IOException.class, BindException.class,
            HttpMessageNotReadableException.class, MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class, MissingServletRequestPartException.class,
            TypeMismatchException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage badRequest(Exception ex) {
        return new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(), LocalDate.now(),
                ex.getMessage(),messageSource.getMessage("bad.request", null, Locale.getDefault()));
    }

    @ExceptionHandler(value = { NoSuchRequestHandlingMethodException.class,
            NoHandlerFoundException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage notFound(Exception ex) {
        return new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), LocalDate.now(),
                ex.getMessage(), messageSource.getMessage("page.not.found", null, Locale.getDefault()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorMessage unSupportedMediaType(Exception ex) {
        return new ErrorMessage(Response.Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode(), LocalDate.now(),
                ex.getMessage(), messageSource.getMessage("un.supported.type", null, Locale.getDefault()));
    }


    @ExceptionHandler(value = { SQLException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleSQLException(Exception ex) {
        return new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                LocalDate.now(), ex.getMessage(),
                messageSource.getMessage("database.error", null, Locale.getDefault()));
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage internalServerError(Exception ex) {
        return new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                LocalDate.now(), ex.getMessage(),
                messageSource.getMessage("something.went.wrong", null, Locale.getDefault()));
    }

    @ExceptionHandler(value = {JwtException.class})
    public Response unauthorized(Exception ex) {
       return Response.status(Response.Status.UNAUTHORIZED).build();
    }


}
