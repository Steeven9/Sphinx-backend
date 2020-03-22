package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

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
            user = userService.get(givenUser.username);
        } else {
            user = userService.getByMail(givenUser.email);
        }

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getVerified()) {
            return ResponseEntity.status(403).build();
        }
        if (!user.getPassword().equals(givenUser.password)) {
            return ResponseEntity.status(401).build();
        }

        user.createSessionToken();
        if(userService.update(user.getUsername(), user)){
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok(user.getSessionToken());
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
        if (verifiedUser.getVerified()) {
            return ResponseEntity.badRequest().build();
        }
        if (!verifiedUser.getVerificationToken().equals(fakeUser.getVerificationToken())) {
            return ResponseEntity.status(401).build();
        }
        verifiedUser.setVerified(true);
        return ResponseEntity.ok().build();
    }

}
