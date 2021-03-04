package com.poe.webserver;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BaseController {
	@Autowired
	NetworkRepository repository;

	@GetMapping("/")
	public ResponseEntity index() {
		// TODO: Decide how we handle the response for invalid urls
		return new ResponseEntity<>(new BroadcastMessage("Invalid URL", "Error", 0), HttpStatus.NOT_FOUND);
	}

	// Return list of nodes and their availability
	@GetMapping("/api/v1/status")
	public ResponseEntity status() {
		// TODO: get list of nodes from db, check health of each, add to list, return list
		ArrayList<Node> nodes = repository.findAll();
		return new ResponseEntity<>(nodes, HttpStatus.OK);
	}

	@DeleteMapping("/api/v1/delete/all") 
	public ResponseEntity deleteAll() {
		repository.deleteAll();
		return new ResponseEntity<>(new BroadcastMessage("All nodes were deleted", "Info", 0), HttpStatus.ACCEPTED);
	}

	// Add a node to the db, then look up ip addresses of neighbors and call new node
	@PostMapping("/api/v1/add")
	public ResponseEntity addNode(@RequestBody Node node) {
		Node savedNode = repository.save(node);
		return new ResponseEntity<>(savedNode, HttpStatus.OK);
	}

	// Find all the neighbors, remove this node from their connections list, then 
	// remove the node from the db
	@DeleteMapping("/api/v1/delete/{id}")
	public ResponseEntity deleteNode(@PathVariable int id) {
		Node node = repository.findById(id);
		if (node == null) {
			return new ResponseEntity<>(new BroadcastMessage("Could not find node with id " + id, "error", 0), HttpStatus.NOT_FOUND);
		}

		for (int connectionId: node.getConnections()) {
			// TODO: contact node and tell them to remove neighbor
			Node connection = repository.findById(connectionId);
			if (connection != null) {
				connection.removeConnection(id);
				repository.save(connection);
			}
		}
		repository.delete(node);

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	// Get node information from db, then call out to node to check availability.
	@GetMapping("/api/v1/{id}/status")
	public ResponseEntity nodeStatus(@PathVariable int id) {
		Node node = repository.findById(id);
		if (node == null) {
			return new ResponseEntity<>(new BroadcastMessage("Could not find node with id " + id, "error", 0), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(node, HttpStatus.OK);
	}

	// This method takes an id of a node that is in the network
	// It will add that id to the node's list of neighbors
	// It will send a message to the node with the new neighbor node's ip address
	@PostMapping("/api/v1/{id}/add")
	public ResponseEntity addConnection(@RequestBody Integer neighborId, @PathVariable int id) {
		if (neighborId == id) {
			return new ResponseEntity<>(new BroadcastMessage("Cannot add node as neighbor to itself", "error", id), HttpStatus.BAD_REQUEST);
		}

		// get nodes from database
		Node targetNode = repository.findById(id);
		if (targetNode == null) {
			return new ResponseEntity<>(new BroadcastMessage("Could not find node with id " + id, "error", 0), HttpStatus.NOT_FOUND);
		}

		Node neighborNode = repository.findById((int)neighborId);
		if (neighborNode == null) {
			return new ResponseEntity<>(new BroadcastMessage("Could not find node with id " + neighborId, "error", 0), HttpStatus.NOT_FOUND);
		}

		targetNode.addConnection(neighborId);
		neighborNode.addConnection(id);

		// save back to db
		repository.save(targetNode);
		repository.save(neighborNode);

		// return a list with two elements, target and neighbor, to show both connection lists 
		// 	are updated
		ArrayList<Node> updatedNodes = new ArrayList<>();
		updatedNodes.add(targetNode);
		updatedNodes.add(neighborNode);

		return new ResponseEntity<>(updatedNodes, HttpStatus.OK);
	}

	@DeleteMapping("/api/v1/{id}/remove")
	public ResponseEntity removeConnection(@RequestBody Integer neighborId, @PathVariable int id) {
		if (neighborId == id) {
			return new ResponseEntity<>(new BroadcastMessage("A node cannot be its own neighbor", "error", id), HttpStatus.BAD_REQUEST);
		}
		
		Node targetNode = repository.findById(id);
		if (targetNode == null) {
			return new ResponseEntity<>(new BroadcastMessage("Could not find node with id " + id, "error", 0), HttpStatus.NOT_FOUND);
		}

		Node neighborNode = repository.findById((int)neighborId);
		if (neighborNode == null) {
			return new ResponseEntity<>(new BroadcastMessage("Could not find node with id " + neighborId, "error", 0), HttpStatus.NOT_FOUND);
		}

		targetNode.removeConnection(neighborId);
		neighborNode.removeConnection(id);

		repository.save(targetNode);
		repository.save(neighborNode);

		// return a list with two elements, target and neighbor, to show both connection lists 
		// 	are updated
		ArrayList<Node> updatedNodes = new ArrayList<>();
		updatedNodes.add(targetNode);
		updatedNodes.add(neighborNode);

		return new ResponseEntity<>(updatedNodes, HttpStatus.OK);
	}

	// Pull list of nodes from db, make a request to the first to pass along the message
	// If that node is unavailable, continue down list until one responds.
	@PostMapping("/api/v1/broadcast")
	public ResponseEntity broadcast(@RequestBody BroadcastMessage broadcastMessage) {
		ArrayList<Node> nodeList = repository.findAll();
		for (Node node: nodeList) {
			if (attemptBroadcast(node)) {
				return new ResponseEntity<>(new BroadcastMessage("Message broadcast to entry node " + node.getId(), "Info", 0), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(new BroadcastMessage("No response from nodes", "Error", 0), HttpStatus.SERVICE_UNAVAILABLE);
	}

	// internal method used to start a broadcast chain
	private boolean attemptBroadcast(Node node) {
		// TODO: implement the call to the node, asking to pass the message on
		return true;
	}
}