package ch.usi.inf.sa4.sphinx.misc;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void testNotFoundException() {
        assertNotNull(new NotFoundException("something"));
        assertNotNull(new NotFoundException("something", new Exception()));
        NotFoundException exception = new NotFoundException("something");
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getStatus().value(), 404);
        assertEquals(exception.getCode(), 404);
    }

    @Test
    void testBadRequestException() {
        assertNotNull(new BadRequestException("something"));
        assertNotNull(new BadRequestException("something", new Exception()));
        BadRequestException exception = new BadRequestException("something");
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(exception.getStatus().value(), 400);
    }

    @Test
    void testForbiddenException() {
        assertNotNull(new ForbiddenException("something"));
        assertNotNull(new ForbiddenException("something", new Exception()));
        ForbiddenException exception = new ForbiddenException("something");
        assertEquals(exception.getStatus(), HttpStatus.FORBIDDEN);
        assertEquals(exception.getStatus().value(), 403);
    }

    @Test
    void testServerErrorException() {
        assertNotNull(new ServerErrorException("something"));
        assertNotNull(new ServerErrorException("something", new Exception()));
        ServerErrorException exception = new ServerErrorException("something");
        assertEquals(exception.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(exception.getStatus().value(), 500);
    }

    @Test
    void testUnauthorizedException() {
        assertNotNull(new UnauthorizedException("something"));
        assertNotNull(new UnauthorizedException("something", new Exception()));
        UnauthorizedException exception = new UnauthorizedException("something");
        assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);
        assertEquals(exception.getStatus().value(), 401);
    }

    @Test
    void testNotImplementedException() {
        assertNotNull(new NotImplementedException());
    }

    @Test
    void testWrongUniverseException() {
        assertNotNull(new WrongUniverseException());
    }
}