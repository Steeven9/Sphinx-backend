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
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(404, exception.getStatus().value());
        assertEquals(404, exception.getCode());
    }

    @Test
    void testBadRequestException() {
        assertNotNull(new BadRequestException("something"));
        assertNotNull(new BadRequestException("something", new Exception()));
        BadRequestException exception = new BadRequestException("something");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(400, exception.getStatus().value());
    }

    @Test
    void testForbiddenException() {
        assertNotNull(new ForbiddenException("something"));
        assertNotNull(new ForbiddenException("something", new Exception()));
        ForbiddenException exception = new ForbiddenException("something");
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals(403, exception.getStatus().value());
    }

    @Test
    void testServerErrorException() {
        assertNotNull(new ServerErrorException("something"));
        assertNotNull(new ServerErrorException("something", new Exception()));
        ServerErrorException exception = new ServerErrorException("something");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals(500, exception.getStatus().value());
    }

    @Test
    void testUnauthorizedException() {
        assertNotNull(new UnauthorizedException("something"));
        assertNotNull(new UnauthorizedException("something", new Exception()));
        UnauthorizedException exception = new UnauthorizedException("something");
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals(401, exception.getStatus().value());
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