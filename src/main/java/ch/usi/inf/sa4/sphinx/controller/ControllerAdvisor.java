package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.misc.ServerErrorException;
import ch.usi.inf.sa4.sphinx.misc.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleException(UnauthorizedException e) {
        // log exception
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Credentials are not valid: " + e.getMessage());
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity handleException(ServerErrorException e) {
        // log exception
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("oops our fault: " + e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleException(BadRequestException e) {
        // log exception
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Bad request: " + e.getMessage());
    }
}