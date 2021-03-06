package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.misc.BadRequestException;
import ch.usi.inf.sa4.sphinx.misc.ServerErrorException;
import ch.usi.inf.sa4.sphinx.misc.WrongUniverseException;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = {"http://localhost:3000", "https://smarthut.xyz"})
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    Mailer mailer;
    @Autowired
    UserService userService;

    /**
     * Gets a User.
     *
     * @param username      the username of the requested user
     * @param sessionToken the session token used for authentication
     * @return a ResponseEntity with the data of the requested user if successful or
     * status code 404 if no user with the requested username exists
     * status code 401 if the provided session token does not match (or does not exist)
     * @see SerialisableUser
     * @see User
     */
    @GetMapping("/{username}")
    @ApiOperation("Gets a User")
    public ResponseEntity<SerialisableUser> getUser(@PathVariable final String username, @RequestHeader("session-token") final String sessionToken) {


        userService.validateSession(username, sessionToken);

        final User user = userService.get(username).orElseThrow(WrongUniverseException::new);
        return ResponseEntity.ok(user.serialise());
    }

    /**
     * Creates a new user.
     *
     * @param username the username of the user to create
     * @param user     a SerialisableUser with the data of the user to create
     * @return a ResponseEntity with status code 203 and a body with the newly-created user's data if the process was successful or
     * 400 if some data was missing or the usernames do not match
     * @see SerialisableUser
     * @see User
     */
    @PostMapping("/{username}")
    @ApiOperation("Creates a new User")
    public ResponseEntity<SerialisableUser> createUser(@PathVariable final String username, @RequestBody final SerialisableUser user) {
        final User findUser = userService.get(username)
                .orElseGet(() -> userService.getByMail(user.getEmail()).orElse(null));

        if (findUser != null) {
            throw new BadRequestException("This user already exists");
        }

        User newUser = new User(user.getEmail(), user.getPassword(), username, user.getFullname());


        try {
            if (!userService.insert(newUser)) {
                throw new BadRequestException("Check that you're providing username, fullname, password and email");
            }
        } catch (final ConstraintViolationException | DataIntegrityViolationException e) {
            throw new BadRequestException("Some fields are missing", e);
        }
        newUser = userService.get(username).orElseThrow(() -> new ServerErrorException("Couldn't save data"));



        try {
            mailer.send(newUser.getEmail(),
                    "Confirm your email account for SmartHut",
                    "Visit this link to confirm your email address: https://smarthut.xyz/verification?email=" + newUser.getEmail() + "&code=" + newUser.getVerificationToken() +
                            "\nOr, from local, http://localhost:3000/verification?email=" + newUser.getEmail() + "&code=" + newUser.getVerificationToken());
        } catch (final MailException e) {
            throw new BadRequestException("Please insert a valid email", e);
        }

        return ResponseEntity.status(201).body(newUser.serialise());
    }


    /**
     * Changes a user's information.
     *
     * @param username      the username of the user to change
     * @param user          a SerialisableUser containing the new data of the user
     * @param sessionToken the session token used for authentication
     * @param errors        validation errors
     * @return a ResponseEntity with status 200 and body containing the data of the changed user or
     * 404 if no user with the requested username exists
     * 401 if the provided session token does not match the requested user (or none was provided)
     * @see SerialisableUser
     * @see User
     */
    @Transactional
    @PutMapping("/{username}")
    @ApiOperation("Modifies a User")
    public ResponseEntity<SerialisableUser> updateUser(@NotBlank @PathVariable final String username, @NotNull @RequestBody final SerialisableUser user,
                                                       @RequestHeader("session-token") final String sessionToken, final Errors errors) {

        if (errors.hasErrors()) {
            throw new BadRequestException("Some fields are missing");
        }

        userService.validateSession(username, sessionToken);

        final User changedUser = userService.get(username).orElseThrow(WrongUniverseException::new);

        if (user.getEmail() != null) changedUser.setEmail(user.getEmail());
        if (user.getFullname() != null) changedUser.setFullname(user.getFullname());
        if (user.getPassword() != null) changedUser.setPassword(user.getPassword());
        if (user.getAllowSecurityCameras()!= null) changedUser.switchCamerasAccessibility(user.getAllowSecurityCameras());

        userService.update(changedUser);
        if (user.getUsername() != null && !username.equals(user.getUsername())) {
            userService.changeUsername(username, user.getUsername());
        }
        User user1 = userService.getById(changedUser.getId()).orElseThrow(WrongUniverseException::new);
        return ResponseEntity.ok(user1.serialise());
    }

    /**
     * Deletes a user.
     *
     * @param username      the username of the user to delete
     * @param sessionToken the session token used to authenticate
     * @return a ResponseEntity containing one of the following status codes:
     * 404 if no user with the given username exists
     * 401 if the session token does not match
     * 204 if the operation was successful
     * @see User
     */
    @DeleteMapping("/{username}")
    @ApiOperation("Deletes a User")
    public ResponseEntity<SerialisableUser> deleteUser(@PathVariable final String username,
                                                       @RequestHeader("session-token") final String sessionToken) {

        userService.validateSession(username, sessionToken);

        userService.delete(username);
        return ResponseEntity.noContent().build();
    }
}

