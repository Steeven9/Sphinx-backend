package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.misc.*;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

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
     * @return A ResponseEntity containing status code 200 if the username and session token match or
     * status 404 if no user with the given username exists
     * status 401 id the username and session token do not match
     */
    @PostMapping("/validate")
    public ResponseEntity<String> validate(@RequestHeader("user") String username,
                                           @RequestHeader("session-token") String sessionToken) {


        User user;

        user = userService.get(username)
                .orElse(userService.getByMail(username).orElse(null));

        if(user == null){
            throw new UnauthorizedException("");
        }

        if (!userService.validSession(user.getUsername(), sessionToken)) {
            throw new UnauthorizedException("");
        }

        return ResponseEntity.ok().body(user.getUsername());
    }


    /**
     * Logs in using given credentials.
     *
     * @param username the username or email of the user to log in
     * @param password the password of the user to log in
     * @return A ResponseEntity containing status code 200 and the new session token as a body if successful or
     * status 400 if no username or email is provided
     * status 404 if provided username or email do not correspond to any user
     * status 403 if the user is not verified yet
     * status 401 if the password was incorrect
     */
    @PostMapping("/login/{username}")
    public ResponseEntity<String> login(@NotBlank @PathVariable String username, @NotBlank @RequestBody String password, Errors errors) {

        if (errors.hasErrors()) {
            throw new BadRequestException("Check if all the required fields are not blank");
        }
        User user;

        user = userService.get(username)
                .orElse(userService.getByMail(username).orElse(null));

        if(user == null){
            throw new UnauthorizedException("");
        }

        if (!user.isVerified()) {
            throw new ForbiddenException("");
        }

        if (!user.getPassword().equals(password)) {
            throw new UnauthorizedException("");
        }

        user.createSessionToken();
        if (!userService.update(user)) {
            throw new ServerErrorException("");
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
     * 200 if otherwise
     */
    @PostMapping("/verify/{email}")
    public ResponseEntity<SerialisableUser> verifyUser(@PathVariable String email, @RequestBody String verificationCode) {
        User verifiedUser = userService.getByMail(email).orElseThrow(() -> new NotFoundException("User not found"));

        if (verifiedUser.isVerified()) {
            throw new BadRequestException("User is already verified");
        }
        if (!verifiedUser.getVerificationToken().equals(verificationCode)) {
            throw new UnauthorizedException("verificationCode is not valid");
        }

        verifiedUser.setVerified(true);
        if (!userService.update(verifiedUser)) {
            throw new ServerErrorException("server failed to update user verification");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Generates and sends a reset code used to reset a User's password for a particular email address.
     *
     * @param email the email address of the User whose password needs to be reset
     * @return A ResponseEntity containing status code 204 if operation completed successfully or
     * 404 id the provided email address does not exist.
     */
    @PostMapping("/reset/{email}")
    public ResponseEntity<Boolean> resetUser(@PathVariable String email) {
        User resetUser = userService.getByMail(email)
                .orElseThrow(() -> new NotFoundException("user not found"));


        String resetCode = resetUser.createResetCode();

        mailer.send(email,
                "Reset your password on smarthut.xyz",
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
     * @return A responseEntity containing status code 204 if the operation completed successfully or
     * 404 if the provided email address does not exist
     * 403 if the provided reset code does not match the true code.
     */
    @PostMapping("/reset/{email}/{resetCode}")
    public ResponseEntity<Boolean> changePassword(@NotBlank @PathVariable String email, @NotBlank @PathVariable String resetCode,
                                                  @NotBlank @RequestBody String newPassword, Errors errors) {

        if (errors.hasErrors()) {
            throw new BadRequestException("email,resetCode or password is blank or missing");
        }

        User changedUser = userService.getByMail(email)
                .orElseThrow(() -> new NotFoundException("user not found"));

        if (!resetCode.equals(changedUser.getResetCode())) {
            throw new UnauthorizedException("wrong reset code");
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
     * 404 id the provided email address does not exist.
     */
    @PostMapping("/resend/{email}")
    public ResponseEntity<Boolean> resendEmailVerification(@PathVariable String email) {
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.getByMail(email)
                .orElseThrow(() -> new NotFoundException("no user with this mail"));

        if (user.isVerified()) {
            throw new BadRequestException("User is already verified");
        }

        mailer.send(email,
                "Confirm your email account for SmartHut",
                "Visit this link to confirm your email address: https://smarthut.xyz/verification?email=" + email + "&code=" + user.getVerificationToken() +
                        "\nOr, from local, http://localhost:3000/verification?email=" + email + "&code=" + user.getVerificationToken());

        return ResponseEntity.status(204).build();
    }
}
