package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.misc.HttpException;
import ch.usi.inf.sa4.sphinx.misc.WrongUniverseException;
import ch.usi.inf.sa4.sphinx.view.SerialisableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * This class catches the Exceptions specified in the methods tagged as 'ExceptionHandler' and turns them
 * into the appropriate ResponseEntity.
 */
@ControllerAdvice
public final class ControllerAdvisor {
    private ControllerAdvisor(){}
    @ExceptionHandler(HttpException.class)
    public static ResponseEntity<SerialisableException> handleHttpException(final HttpException e) {
        // log exception
        return ResponseEntity
                .status(e.getStatus())
                .body(new SerialisableException(e.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(WrongUniverseException.class)
    public static ResponseEntity<SerialisableException> handleWrongUniverseException(final HttpException e) {
        // log exception
        return ResponseEntity
                .status(e.getStatus())
                .body(new SerialisableException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}