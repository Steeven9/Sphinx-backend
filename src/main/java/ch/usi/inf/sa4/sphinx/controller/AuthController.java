package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

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
        if (!user.verified) {
            return ResponseEntity.status(403).build();
        }
        if (user.password != givenUser.password) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(user.createSessionToken()).build();
    }

    @PostMapping("/verify")
    public ResponseEntity<SerialisableUser> verifyUser(@RequestBody User fakeUser) {
        User verifiedUser = Storage.getUser(fakeUser.username);
        if (verifiedUser == null) {
            return ResponseEntity.notFound().build();
        }
        if (verifiedUser.verified) {
            return ResponseEntity.badRequest().build();
        }
        if (verifiedUser.verificationToken != fakeUser.verificationToken) {
            return ResponseEntity.status(401).build();
        }
        verifiedUser.verified = true;
        return ResponseEntity.ok().build();
    }

}
