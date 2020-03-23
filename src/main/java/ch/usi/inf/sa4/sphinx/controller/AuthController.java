package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/auth")
public class AuthController {

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
    public ResponseEntity<String> login(@PathVariable String username, @RequestBody String password) {
        User user;
        user = Storage.getUser(username);
        if (user == null) {
            user = Storage.getUserByEmail(username);
        }

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.isVerified()) {
            return ResponseEntity.status(403).build();
        }
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(user.createSessionToken());
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
        User verifiedUser = Storage.getUserByEmail(email);
        if (verifiedUser == null) {
            return ResponseEntity.notFound().build();
        }
        if (verifiedUser.isVerified()) {
            return ResponseEntity.badRequest().build();
        }
        if (!verifiedUser.getVerificationToken().equals(verificationCode)) {
            return ResponseEntity.status(401).build();
        }
        verifiedUser.verify();
        return ResponseEntity.ok().build();
    }

}
