package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
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
     * @param username the username of the user to authenticate
     * @param sessionToken the session token to validate
     * @return A ResponseEntity containing status code 200 if the username and session token match or
     *      status 404 if no user with the given username exists
     *      status 401 id the username and session token do not match
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader("user") String username,
                                            @RequestHeader("session-token") String sessionToken) {
        User user = userService.get(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getSessionToken().equals(sessionToken)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok().build();
    }


    /**
     * Logs in using given credentials.
     * @param username the username or email of the user to log in
     * @param password the password of the user to log in
     * @return A ResponseEntity containing status code 200 and the new session token as a body if successful or
     *      status 400 if no username or email is provided
     *      status 404 if provided username or email do not correspond to any user
     *      status 403 if the user is not verified yet
     *      status 401 if the password was incorrect
     */
    @PostMapping("/login/{username}")
    public ResponseEntity<String> login(@NotBlank @PathVariable String username, @NotBlank @RequestBody String password, Errors errors) {

        if (errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        User user;

        user = userService.get(username);
        if (user == null) {
            user = userService.getByMail(username);
        }

        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        if (!user.isVerified()) {
            return ResponseEntity.status(403).build();
        }

        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).build();
        }

        user.createSessionToken();
        if(!userService.update(user)){
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok(user.getSessionToken());
    }

    /**
     * Verifies a user's email address.
     * @param email the email address of the user to verify
     * @param verificationCode the verification code of the user requesting verification
     * @return A ResponseEntity with one of the following status codes:
     *      404 if no user with the given username exists
     *      400 if the user with the given username is already verified
     *      401 if the given verificationToken does not match the one of the user with the given username
     *      200 if otherwise
     */
    @PostMapping("/verify/{email}")
    public ResponseEntity<SerialisableUser> verifyUser(@PathVariable String email, @RequestBody String verificationCode) {
        User verifiedUser = userService.getByMail(email);
        if (verifiedUser == null) {
            return ResponseEntity.notFound().build();
        }
        if (verifiedUser.isVerified()) {
            return ResponseEntity.badRequest().build();
        }
        if (!verifiedUser.getVerificationToken().equals(verificationCode)) {
            return ResponseEntity.status(401).build();
        }
        verifiedUser.setVerified(true);
        if (!userService.update(verifiedUser)) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Generates and sends a reset code used to reset a User's password for a particular email address.
     * @param email the email address of the User whose password needs to be reset
     * @return A ResponseEntity containing status code 204 if operation completed successfully or
     *      404 id the provided email address does not exist.
     */
    @PostMapping("/reset/{email}")
    public ResponseEntity<Boolean> resetUser(@PathVariable String email) {
        User resetUser = userService.getByMail(email);

        if (resetUser == null) {
            return ResponseEntity.notFound().build();
        }

        String resetCode = resetUser.createResetCode();

        mailer.send(email,
                "Reset your password active smarthut.xyz",
                "Visit this link to reset your password: https://smarthut.xyz/changepassword?email=" + email + "&code=" + resetCode +
                "\nOr, from local, http://localhost:3000/changepassword?email=" + email + "&code=" + resetCode);

        return ResponseEntity.noContent().build();
    }

    /**
     * Given the correct resetCode for the given email address, changes its password to the given password.
     * @param email the email of the User whose password should be changed
     * @param resetCode the resetCode of the given user
     * @param newPassword the new password to change to
     * @return A responseEntity containing status code 204 if the operation completed successfully or
     *      404 if the provided email address does not exist
     *      403 if the provided reset code does not match the true code.
     */
    @PostMapping("/reset/{email}/{resetCode}")
    public ResponseEntity<Boolean> changePassword(@PathVariable String email, @PathVariable String resetCode,
                                                  @RequestBody String newPassword) {
        User changedUser = userService.getByMail(email);

        if (changedUser == null) {
            return ResponseEntity.notFound().build();
        }
        if (resetCode == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!resetCode.equals(changedUser.getResetCode())) {
            return ResponseEntity.status(403).build();
        }

        changedUser.setPassword(newPassword);
        return ResponseEntity.noContent().build();
    }

}
