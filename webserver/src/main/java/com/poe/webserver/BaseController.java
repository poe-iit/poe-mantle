package com.poe.webserver;

import org.springframework.web.bind.annotation.*;

@RestController
public class BaseController {
	@GetMapping("/")
	public String index() {
		return "Error: Invalid URL";
	}

	@GetMapping("/api/v1/status")
	public String status() {
		return "Test Response!";
	}

	@PostMapping("/api/v1/add")
	public String addNode(@RequestBody Node node) {
		return "Success: " + node.name;
	}

	@GetMapping("/api/v1/{id}/status")
	public String nodeStatus(@PathVariable int id) {
		return "Success: " + id;
	}

	@GetMapping("/api/v1/broadcast")
	public String broadcast(@RequestBody BroadcastMessage broadcastMessage) {
		return "Success: " + broadcastMessage.message;
	}
}