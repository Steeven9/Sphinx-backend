package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.misc.*;
import ch.usi.inf.sa4.sphinx.view.SerialisableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * This class catches the Exceptions specified in the methods tagged as 'ExceptionHandler' and turns them
 * into the appropriate ResponseEntity.
 */
@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<SerialisableException> handleException(HttpException e) {
        // log exception
        return ResponseEntity
                .status(e.getStatus())
                .body(new SerialisableException(e.getStatus(), e.getMessage()));
    }
}