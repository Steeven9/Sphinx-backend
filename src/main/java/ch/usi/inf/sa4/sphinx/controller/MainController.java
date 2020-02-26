package ch.usi.inf.sa4.sphinx.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {
	@GetMapping("/")
	public ResponseEntity<Boolean> index() {
		return ResponseEntity.noContent().build();
	}
	/*
	@GetMapping("/{id}")
	public ResponseEntity<Message> getById(@PathVariable int id, @RequestParam(defaultValue = "World") String name) {
		if(messages.containsKey(id)){
			var msg = messages.get(id);
			return ResponseEntity.ok(msg);
		}
		return ResponseEntity.notFound().build();
	}
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

