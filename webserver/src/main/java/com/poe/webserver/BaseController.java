package com.poe.webserver;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BaseController {
	@Autowired
	NetworkRepository repository;

	@GetMapping("/")
	public ResponseMessage index() {
		// TODO: Decide how we handle the response for invalid urls
		return new ResponseMessage("Error", "Invalid URL");
	}

	// Return list of nodes and their availability
	@GetMapping("/api/v1/status")
	public ArrayList<Node> status() {
		// TODO: get list of nodes from db, check health of each, add to list, return list
		ArrayList<Node> nodes = new ArrayList<>();
		return nodes;
	}

	// Add a node to the db, then look up mac addresses of neighbors and call new node
	@PostMapping("/api/v1/add")
	public Node addNode(@RequestBody Node node) {
		Node savedNode = repository.save(node);
		return savedNode;
	}

	// Get node information from db, then call out to node to check availability.
	@GetMapping("/api/v1/{id}/status")
	public Node nodeStatus(@PathVariable int id) {
		// TODO: Actually get this node from the database
		Node node = repository.findById(id);
		return node;
	}

	// This method takes an id of a node that is in the network
	// It will add that id to the node's list of neighbors
	// It will send a message to the node with the new neighbor node's mac address
	@PostMapping("/api/v1/{id}/add")
	public Node addConnection(@RequestBody Integer neighborId, @PathVariable int id) {
		// TODO: Add neighbor node id to list of ids, and look up mac address
		Node node = new Node("Garage", "87:65:43:21", 1, null);
		node.setConnections(new int[] {neighborId});
		return node;
	}

	// Pull list of nodes from db, make a request to the first to pass along the message
	// If that node is unavailable, continue down list until one responds.
	@PostMapping("/api/v1/broadcast")
	public ResponseMessage broadcast(@RequestBody BroadcastMessage broadcastMessage) {
		// TODO: Entry node will be the first node to respond
		return new ResponseMessage("Success", "Message broadcast to entry node " + 1);
	}
}