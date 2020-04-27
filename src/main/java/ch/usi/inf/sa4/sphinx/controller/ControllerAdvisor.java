package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.misc.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity handleException(HttpException e) {
        // log exception
        return ResponseEntity
                .status(e.getStatus())
                .body(e.getMessage());
    }

}