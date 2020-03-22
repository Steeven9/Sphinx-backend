package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    Mailer mailer;

    /**
     * Logs in using given credentials.
     * @param givenUser a SerialisableUser containing the credentials provided by the client, parsed from json.
     * @return A ResponseEntity containing status code 200 and the new session token as a body if successful or
     *      status 400 if no username or email is provided
     *      status 404 if provided username or email do not correspond to any user
     *      status 403 if the user is not verified yet
     *      status 401 if the password was incorrect
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody SerialisableUser givenUser) {
        if (givenUser.email == null && givenUser.username == null) {
            return ResponseEntity.status(400).build();
        }

        User user;
        if (givenUser.username != null) {
            user = Storage.getUser(givenUser.username);
        } else {
            user = Storage.getUserByEmail(givenUser.email);
        }

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.isVerified()) {
            return ResponseEntity.status(403).build();
        }
        if (!user.getPassword().equals(givenUser.password)) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(user.createSessionToken());
    }

    /**
     * Verifies a user's email address.
     * @param fakeUser an empty User containing the username and verificationToken provided by the client
     * @return A ResponseEntity with one of the following status codes:
     *      404 if no user with the given username exists
     *      400 if the user with the given username is already verified
     *      401 if the given verificationToken does not match the one of the user with the given username
     *      200 if otherwise
     */
    @PostMapping("/verify")
    public ResponseEntity<SerialisableUser> verifyUser(@RequestBody User fakeUser) {
        User verifiedUser = Storage.getUser(fakeUser.getUsername());
        if (verifiedUser == null) {
            return ResponseEntity.notFound().build();
        }
        if (verifiedUser.isVerified()) {
            return ResponseEntity.badRequest().build();
        }
        if (!verifiedUser.getVerificationToken().equals(fakeUser.getVerificationToken())) {
            return ResponseEntity.status(401).build();
        }
        verifiedUser.verify();
        return ResponseEntity.ok().build();
    }

    /**
     * Generates and sends a reset code used to reset a User's password for a particular email address
     * @param email the email address of the User whose password needs to be reset
     * @return A ResponseEntity containing status code 204 if operation completed successfully or
     *      404 id the provided email address does not exist.
     */
    @PostMapping("/reset/{email}")
    public ResponseEntity<Boolean> resetUser(@PathVariable String email) {
        User resetUser = Storage.getUserByEmail(email);

        if (resetUser == null) {
            return ResponseEntity.notFound().build();
        }

        String resetCode = resetUser.createResetCode();

        // TODO: use the actual route provided by the frontend
        mailer.send(email,
                "Reset your password on smarthut.xyz",
                "Visit this link to reset your password: https://smarthut.xyz/auth/change/" + email + "/" + resetCode);

        return ResponseEntity.noContent().build();
    }

    /**
     * Given the correct resetCode for the given email address, changes its password to the given password
     * @param email the email of the User whose password should be changed
     * @param resetCode the resetCode of the given user
     * @param newPassword the new password to change to
     * @return A responseEntity containing status code 204 if the operation completed successfully or
     *      404 if the provided email address does not exist
     *      403 if the provided reset code does not match the true code.
     */
    // TODO: maybe make this a subroute of /reset
    @PostMapping("/change/{email}/{resetCode}")
    public ResponseEntity<Boolean> changePassword(@PathVariable String email, @PathVariable String resetCode,
                                                  @RequestBody String newPassword) {
        User changedUser = Storage.getUserByEmail(email);

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
