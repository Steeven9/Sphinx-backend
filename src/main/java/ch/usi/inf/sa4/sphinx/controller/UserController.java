package ch.usi.inf.sa4.sphinx.controller;


import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
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

	private HashMap<String, User> users = new HashMap<>();

//	@GetMapping("/")
//	public ResponseEntity<Boolean> index() {
//		return ResponseEntity.noContent().build();
//	}

	@GetMapping("/{username}")
	public ResponseEntity<SerialisableUser> getUser(@PathVariable String username) {

		User user = users.get(username);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		if (session_token != user.session_token) {
			return ResponseEntity.status(401).build();
		}
		return ResponseEntity.ok(new SerialisableUser(user));
	}

	@PostMapping("/{username}")
	public ResponseEntity<SerialisableUser> createUser(HttpServletRequest req, @PathVariable String username, @RequestBody SerialisableUser user) {
		if (user.email == null || user.fullname == null || user.password == null || !Objects.equals(user.username, username)) {
			return ResponseEntity.badRequest().build();
		}
		User newUser = new User(username, user.password, user.email, user.fullname);
		users.put(username, newUser);

		try {
			return ResponseEntity.created(new URI(req.getRequestURL() + "/" + username)).body(new SerialisableUser(newUser));
		} catch (URISyntaxException e) {
			System.err.println("The universe broke");
			throw new RuntimeException("The universe broke");
		}
	}

	@PutMapping("/{username}")
	public ResponseEntity<SerialisableUser> updateUser(@PathVariable String username, @RequestBody SerialisableUser user,
													@RequestHeader("session-token") String session_token) {
		User changedUser = users.get(username);

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
		User deletedUser = users.get(username);

		if (deletedUser == null) {
			return ResponseEntity.notFound().build();
		}
		if (session_token == null || session_token != deletedUser.session_token) {
			return ResponseEntity.status(403).build();
		}

		users.remove(username);

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

