package com.poe.webserver;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class BaseController {

	@GetMapping("/api/v1/status")
	public String index() {
		return "Test Response!";
	}

	@PostMapping("/api/v1/add")
	public String addNode(@RequestBody Node node) {
		return "Success: " + node.name;
	}

}