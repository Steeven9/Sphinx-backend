package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.misc.ForbiddenException;
import ch.usi.inf.sa4.sphinx.misc.ServerErrorException;
import ch.usi.inf.sa4.sphinx.misc.UnauthorizedException;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    Mailer mailer;
    @Autowired
    UserService userService;

    /**
     * Validates a given session token
     *
     * @param username     the username of the user to authenticate
     * @param sessionToken the session token to validate
     * @return A ResponseEntity containing status code 200 on success if the username and session token match or
     * status 401  if no user with the given username exists
     * status 200 id the username and session token do not match
     * @see User
     */
    @PostMapping("/validate")
    @ApiOperation("Validates a given session token")
    public ResponseEntity<String> validate(@RequestHeader("user") final String username,
                                           @RequestHeader("session-token") final String sessionToken) {


        final User user;

        user = userService.get(username)
                .orElseGet(() -> userService.getByMail(username).orElseThrow(() -> new UnauthorizedException("Invalid credentials")));

        userService.validateSession(user.getUsername(), sessionToken);

        return ResponseEntity.ok().body(user.getUsername());
    }


    /**
     * Logs in using given credentials.
     *
     * @param username the username or email of the user to log in
     * @param password the password of the user to log in
     * @param errors   validation errors
     * @return A ResponseEntity containing status code 200 and the new session token as a body if successful or
     * status 400 if no username or email is provided
     * status 403 if the user is not verified yet
     * status 401 if provided username or email do not match  any user
     * status 401 if the password was incorrect
     * @see User
     */
    @PostMapping("/login/{username}")
    public ResponseEntity<String> login(@NotBlank @PathVariable final String username, @NotBlank @RequestBody final String password, final Errors errors) {

        if (errors.hasErrors()) {
            throw new BadRequestException("Some fields are missing");
        }
        final User user;

        user = userService.get(username)
                .orElseGet(() -> userService.getByMail(username).orElseThrow(() -> new UnauthorizedException("Invalid credentials")));

        if (!user.isVerified()) {
            throw new ForbiddenException("User is not verified");
        }


        if (!user.matchesPassword(password)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        user.createSessionToken();
        if (!userService.update(user)) {
            throw new ServerErrorException("Couldn't save data");
        }

        return ResponseEntity.ok(user.getSessionToken());
    }

    /**
     * Verifies a user's email address.
     *
     * @param email            the email address of the user to verify
     * @param verificationCode the verification code of the user requesting verification
     * @return A ResponseEntity with one of the following status codes:
     * 404 if no user with the given username exists
     * 400 if the user with the given username is already verified
     * 401 if the given verificationToken does not match the one of the user with the given username
     * 200 if otherwise hence success
     * @see User
     */
    @PostMapping("/verify/{email}")
    public ResponseEntity<SerialisableUser> verifyUser(@PathVariable final String email, @RequestBody final String verificationCode) {
        final User verifiedUser = userService.getByMail(email).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (verifiedUser.isVerified()) {
            throw new BadRequestException("User is already verified");
        }
        if (!verifiedUser.getVerificationToken().equals(verificationCode)) {
            throw new UnauthorizedException("Invalid verification code");
        }

        verifiedUser.setVerified(true);
        if (!userService.update(verifiedUser)) {
            throw new ServerErrorException("Couldn't save data");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Generates and sends a reset code used to reset a User's password for a particular email address.
     *
     * @param email the email address of the User whose password needs to be reset
     * @return A ResponseEntity containing status code 204 if operation completed successfully or
     * 404 id the provided email address does not exist.
     * @see User
     */
    @PostMapping("/reset/{email}")
    public ResponseEntity<Boolean> resetUser(@PathVariable final String email) {
        final User resetUser = userService.getByMail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));


        final String resetCode = resetUser.createResetCode();

        mailer.send(email,
                "Reset your SmartHut password",
                "Visit this link to reset your password: https://smarthut.xyz/changepassword?email=" + email + "&code=" + resetCode +
                        "\nOr, from local, http://localhost:3000/changepassword?email=" + email + "&code=" + resetCode);

        return ResponseEntity.noContent().build();
    }

    /**
     * Given the correct resetCode for the given email address, changes its password to the given password.
     *
     * @param email       the email of the User whose password should be changed
     * @param resetCode   the resetCode of the given user
     * @param newPassword the new password to change to
     * @param errors      validation errors
     * @return A responseEntity containing status code 204 if the operation completed successfully or
     * 404 if the provided email address does not exist
     * 403 if the provided reset code does not match the true code.
     * @see User
     */
    @PostMapping("/reset/{email}/{resetCode}")
    public ResponseEntity<Boolean> changePassword(@NotBlank @PathVariable final String email, @NotBlank @PathVariable final String resetCode,
                                                  @NotBlank @RequestBody final String newPassword, final Errors errors) {

        if (errors.hasErrors()) {
            throw new BadRequestException("Some fields are missing");
        }

        final User changedUser = userService.getByMail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!resetCode.equals(changedUser.getResetCode())) {
            throw new UnauthorizedException("Invalid reset code");
        }


        changedUser.setPassword(newPassword);
        if (!userService.update(changedUser)) {
            throw new ServerErrorException("");
        }



        return ResponseEntity.noContent().build();

    }

    /**
     * Re-sends a verification email to a User.
     *
     * @param email the email address of the User
     * @return A ResponseEntity containing status code 204 if operation completed successfully or
     * 404 id the provided email address does not exist
     * @see User
     */
    @PostMapping("/resend/{email}")
    public ResponseEntity<Boolean> resendEmailVerification(@NotBlank @PathVariable final String email) {
        final User user = userService.getByMail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (user.isVerified()) {
            throw new BadRequestException("User is already verified");
        }

        mailer.send(email,
                "Confirm your email account for SmartHut",
                "Visit this link to confirm your email address: https://smarthut.xyz/verification?email=" + email + "&code=" + user.getVerificationToken() +
                        "\nOr, from local, http://localhost:3000/verification?email=" + email + "&code=" + user.getVerificationToken());

        return ResponseEntity.noContent().build();
    }
}
