package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

	@GetMapping("/{username}")
	public ResponseEntity<SerialisableUser> getUser(@PathVariable String username, @RequestHeader("session-token") String session_token) {

		User user = Storage.getUser(username);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		if (session_token != user.session_token) {
			return ResponseEntity.status(401).build();
		}
		return ResponseEntity.ok(new SerialisableUser(user));
	}

	@PostMapping("/{username}")
	public ResponseEntity<SerialisableUser> createUser(@PathVariable String username, @RequestBody SerialisableUser user) {
		if (user.email == null || user.fullname == null || user.password == null || !Objects.equals(user.username, username)) {
			return ResponseEntity.badRequest().build();
		}
		User newUser = new User(username, user.password, user.email, user.fullname);
		Storage.insertUser(newUser);

		return ResponseEntity.status(203).body(new SerialisableUser(newUser));
	}

	@PutMapping("/{username}")
	public ResponseEntity<SerialisableUser> updateUser(@PathVariable String username, @RequestBody SerialisableUser user,
													@RequestHeader("session-token") String session_token) {
		User changedUser = Storage.getUser(username);

		if (changedUser == null) {
			return ResponseEntity.notFound().build();
		}
		if (session_token == null || session_token != changedUser.session_token) {
			return ResponseEntity.status(403).build();
		}
		if (user.email != null) changedUser.email = user.email;
		if (user.fullname != null) changedUser.fullname = user.fullname;
		if (user.password != null) changedUser.password = user.password;

		return ResponseEntity.ok(new SerialisableUser(changedUser));
	}

	@DeleteMapping("/{username}")
	public ResponseEntity<SerialisableUser> deleteUser(@PathVariable String username,
													   @RequestHeader("session-token") String session_token) {
		User deletedUser = Storage.getUser(username);

		if (deletedUser == null) {
			return ResponseEntity.notFound().build();
		}
		if (session_token == null || session_token != deletedUser.session_token) {
			return ResponseEntity.status(403).build();
		}

		Storage.deleteUser(username);

		return ResponseEntity.noContent().build();
	}

	/*
	@PostMapping
	public ResponseEntity<Message> postMessage(@RequestBody Message msg){
		logger.debug(msg.toString());
		var nextId = idCounter.getAndIncrement();
		msg.setId(nextId);
		msg.setRandomNumber();
		messages.put(nextId, msg);
		return ResponseEntity.ok(msg);
	}
	*/
}

