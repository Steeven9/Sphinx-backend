package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ch.usi.inf.sa4.sphinx.service.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	Mailer mailer;
	@Autowired
	UserService userService;

	/**
	 * gets a user.
	 * @param username the username of the requested user
	 * @param session_token the session token used for authentication
	 * @return a ResponseEntity with the data of the requested user if successful or
	 * 		status code 404 if no user with the requested username exists
	 * 		status code 401 if the provided session token does not match (or does not exist)
	 */
	@GetMapping("/{username}")
	public ResponseEntity<SerialisableUser> getUser(@PathVariable String username, @RequestHeader("session-token") String session_token) {

		User user = userService.get(username);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		if (session_token == null || !session_token.equals(user.getSessionToken())) {
			return ResponseEntity.status(401).build();
		}
		return ResponseEntity.ok(new SerialisableUser(user));
	}

	/**
	 * creates a new user.
	 * @param username the username of the user to create
	 * @param user a SerialisableUser with the data of the user to create
	 * @return a ResponseEntity with status code 203 and a body with the newly-created user's data if the process was successful or
	 * 		400 if some data was missing or the usernames do not match
	 */
	@PostMapping("/{username}")
	public ResponseEntity<SerialisableUser> createUser(@PathVariable String username, @RequestBody SerialisableUser user) {
		if (user.email == null || user.fullname == null || user.password == null || !Objects.equals(user.username, username)) {
			return ResponseEntity.badRequest().build();
		}
		User newUser = new User(username, user.password, user.email, user.fullname);
		mailer.send(newUser.getEmail(),
				"Confirm your email account for smarthut",
				"Visit this link to confirm your email address: https://smarthut.xyz/auth/verify/" + newUser.getVerificationToken());
		userService.insert(newUser);

		return ResponseEntity.status(203).body(new SerialisableUser(newUser));
	}

	/**
	 * changes a user's information
	 * @param username the username of the user to change
	 * @param user a SerialisableUser containing the new data of the user
	 * @param session_token the session token used for authentication
	 * @return a ResponseEntity with status 200 and body containing the data of the changed user or
	 * 		404 if no user with the requested username exists
	 * 		403 if the provided session token does not match the requested user (or none was provided)
	 */
	@PutMapping("/{username}")
	public ResponseEntity<SerialisableUser> updateUser(@PathVariable String username, @RequestBody SerialisableUser user,
													@RequestHeader("session-token") String session_token) {
		User changedUser = userService.get(username);

		if (changedUser == null) {
			return ResponseEntity.notFound().build();
		}
		if (session_token == null || !session_token.equals(changedUser.getSessionToken())) {
			return ResponseEntity.status(403).build();
		}
		if (user.email != null) changedUser.setEmail(user.email);
		if (user.fullname != null) changedUser.setFullname(user.fullname);
		if (user.password != null) changedUser.setPassword(user.password);

		userService.update(username, changedUser);

		return ResponseEntity.ok(new SerialisableUser(changedUser));
	}

	/**
	 * deletes a user
	 * @param username the username of the user to delete
	 * @param session_token the session token used to authenticate
	 * @return a ResponseEntity containing one of the following status codes:
	 * 		404 if no user with the given username exists
	 * 		403 if the session token does not match
	 * 		204 if the operation was successful
	 */
	@DeleteMapping("/{username}")
	public ResponseEntity<SerialisableUser> deleteUser( @PathVariable String username,
													   @RequestHeader("session-token") String session_token) {
		User deletedUser = userService.get(username);

		if (deletedUser == null) {
			return ResponseEntity.notFound().build();
		}
		if (session_token == null || !session_token.equals(deletedUser.getSessionToken())) {
			return ResponseEntity.status(403).build();
		}

		userService.delete(username);

		return ResponseEntity.noContent().build();
	}
}

