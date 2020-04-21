package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.misc.ServerErrorException;
import ch.usi.inf.sa4.sphinx.misc.UnauthorizedException;
import ch.usi.inf.sa4.sphinx.model.Serialiser;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    Mailer mailer;
    @Autowired
    UserService userService;
    @Autowired
    Serialiser serialiser;

    /**
     * Gets a user.
     *
     * @param username      the username of the requested user
     * @param session_token the session token used for authentication
     * @return a ResponseEntity with the data of the requested user if successful or
     * status code 404 if no user with the requested username exists
     * status code 401 if the provided session token does not match (or does not exist)
     */
    @GetMapping("/{username}")
    public ResponseEntity<SerialisableUser> getUser(@PathVariable String username, @RequestHeader("session-token") String session_token) {

        Optional<User> user = userService.get(username);
        if (user.isPresent()){
            return userService.validSession(user.get().getUsername(),session_token)?
                    ResponseEntity.ok(serialiser.serialiseUser(user.get()))
                    : ResponseEntity.status(401).build();
        }
        return ResponseEntity.notFound().build();

//THIS GIVES UNCHECKED WARNING USE THE PATTERN ABOVE
//        return user.map(u -> userService.validSession(username, session_token) ?
//                ResponseEntity.ok(serialiser.serialiseUser(u))
//                : ResponseEntity.status(401).build()).orElse(new ResponseEntity<SerialisableUser>(HttpStatus.NOT_FOUND));

    }

    /**
     * Creates a new user.
     *
     * @param username the username of the user to create
     * @param user     a SerialisableUser with the data of the user to create
     * @return a ResponseEntity with status code 203 and a body with the newly-created user's data if the process was successful or
     * 400 if some data was missing or the usernames do not match
     */
    @PostMapping("/{username}")
    public ResponseEntity<SerialisableUser> createUser(@PathVariable String username, @RequestBody SerialisableUser user) {
        User newUser = new User(user.email, user.password, username, user.fullname);

        try{
            userService.insert(newUser);
        }catch (ConstraintViolationException e){
            throw new BadRequestException("", e);
        }
        newUser = userService.get(username).orElseThrow(()->new ServerErrorException(""));
        mailer.send(newUser.getEmail(),
                "Confirm your email account for SmartHut",
                "Visit this link to confirm your email address: https://smarthut.xyz/verification?email=" + newUser.getEmail() + "&code=" + newUser.getVerificationToken() +
                        "\nOr, from local, http://localhost:3000/verification?email=" + newUser.getEmail() + "&code=" + newUser.getVerificationToken());

        return ResponseEntity.status(201).body(serialiser.serialiseUser(newUser));
    }


    /**
     * Changes a user's information.
     *
     * @param username      the username of the user to change
     * @param user          a SerialisableUser containing the new data of the user
     * @param session_token the session token used for authentication
     * @return a ResponseEntity with status 200 and body containing the data of the changed user or
     * 404 if no user with the requested username exists
     * 401 if the provided session token does not match the requested user (or none was provided)
     */
    @Transactional
    @PutMapping("/{username}")
    public ResponseEntity<SerialisableUser> updateUser(@NotBlank @PathVariable String username, @NotNull @RequestBody SerialisableUser user,
                                                       @RequestHeader("session-token") String session_token, Errors errors) {

        if (errors.hasErrors()) {
            throw new BadRequestException("");
        }

        User changedUser = userService.get(username).orElseThrow(()->new NotFoundException("username not found"));

        if (!userService.validSession(username, session_token)) {
            throw new UnauthorizedException("");
        }

        if (user.email != null) changedUser.setEmail(user.email);
        if (user.fullname != null) changedUser.setFullname(user.fullname);
        if (user.password != null) changedUser.setPassword(user.password);

        userService.update(changedUser);
        if (user.username != null && !username.equals(user.username)) {
            userService.changeUsername(username, user.username);
        }

        return ResponseEntity.ok(serialiser.serialiseUser(userService.getById(changedUser.getId()).get()));
    }

    /**
     * Deletes a user.
     *
     * @param username      the username of the user to delete
     * @param session_token the session token used to authenticate
     * @return a ResponseEntity containing one of the following status codes:
     * 404 if no user with the given username exists
     * 401 if the session token does not match
     * 204 if the operation was successful
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<SerialisableUser> deleteUser(@PathVariable String username,
                                                       @RequestHeader("session-token") String session_token) {

        userService.get(username).orElseThrow(()->new NotFoundException("Could not find user"));

        if(!userService.validSession(username, session_token)){
            throw new UnauthorizedException("");
        }

        userService.delete(username);
        return ResponseEntity.noContent().build();
    }
}

