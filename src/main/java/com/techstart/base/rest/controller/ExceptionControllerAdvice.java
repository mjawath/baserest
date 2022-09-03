
package com.techstart.base.rest.controller;

import com.techstart.base.rest.controller.exceptions.BadRequest;
import com.techstart.base.rest.controller.exceptions.ContentConflict;
import com.techstart.base.rest.controller.exceptions.NoContentFound;
import com.techstart.base.rest.controller.exceptions.ServerError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author LENOVO PC
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        return exceptionHandler(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(NoContentFound.class)
    public ResponseEntity<ErrorResponse> noContentFoundExceptionHandler(NoContentFound ex) {
        return exceptionHandler(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandler(NoContentFound ex) {
        return exceptionHandler(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ContentConflict.class)
    public ResponseEntity<ErrorResponse> contentConflictExceptionHandler(NoContentFound ex) {
        return exceptionHandler(ex, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ServerError.class)
    public ResponseEntity<ErrorResponse> serverErrorExceptionHandler(NoContentFound ex) {
        return exceptionHandler(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex,HttpStatus status) {
        if(true) //todo if this is dev environment
            ex.printStackTrace();
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(status.value());
        error.setMessage(getStackTrace(ex));
        return new ResponseEntity<>(error, status);
    }
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
