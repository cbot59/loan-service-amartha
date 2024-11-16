package com.journal.loanserviceamartha;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@ControllerAdvice
class RestResponseEntityExcetionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
        IllegalArgumentException.class, IllegalStateException.class
    })
    ResponseEntity<Object> badRequest(Exception exception, WebRequest request, HttpServletRequest servletRequest) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());
        problemDetail.setType(URI.create(servletRequest.getRequestURI()));
        return handleExceptionInternal(
            exception,
            problemDetail,
            new HttpHeaders(),
            HttpStatusCode.valueOf(400),
            request
        );
    }
}
