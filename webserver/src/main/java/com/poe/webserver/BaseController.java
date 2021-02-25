package com.poe.webserver;

import java.util.*;

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
		ArrayList<Node> nodes = repository.findAll();
		return nodes;
	}

	// Add a node to the db, then look up ip addresses of neighbors and call new node
	@PostMapping("/api/v1/add")
	public Node addNode(@RequestBody Node node) {
		Node savedNode = repository.save(node);
		return savedNode;
	}

	// Get node information from db, then call out to node to check availability.
	@GetMapping("/api/v1/{id}/status")
	public ResponseMessage nodeStatus(@PathVariable int id) {
		Node node = repository.findById(id);
		if (node == null) {
			return new ResponseMessage("Error", "Could not find node with id " + id);
		}
		return new NodeResponseMessage("Success", "Node found", node);
	}

	// This method takes an id of a node that is in the network
	// It will add that id to the node's list of neighbors
	// It will send a message to the node with the new neighbor node's ip address
	@PostMapping("/api/v1/{id}/add")
	public ResponseMessage addConnection(@RequestBody Integer neighborId, @PathVariable int id) {

		if (neighborId == id) {
			return new ResponseMessage("Error", "Cannot add node as neighbor to itself");
		}

		// get nodes from database
		Node targetNode = repository.findById(id);
		if (targetNode == null) {
			return new ResponseMessage("Error", "Could not find node with id " + id);
		}

		Node neighborNode = repository.findById((int)neighborId);
		if (neighborNode == null) {
			return new ResponseMessage("Error", "Could not find node with id " + neighborId);
		}

		// pull out connection ids into ArrayLists for easy manipulation
		Set<Integer> targetNodeConnections = new HashSet<>();
		for (int con: targetNode.getConnections()) {
			targetNodeConnections.add(con);
		}
		Set<Integer> neighborNodeConnections = new HashSet<>();
		for (int con: neighborNode.getConnections()) {
			neighborNodeConnections.add(con);
		}

		// add the nodes to each others' connection lists
		targetNodeConnections.add(neighborNode.getId());
		neighborNodeConnections.add(targetNode.getId());
		
		// set the nodes connection lists to bigger happier families
		targetNode.setConnections(targetNodeConnections.stream().mapToInt(Integer::intValue).toArray());
		neighborNode.setConnections(neighborNodeConnections.stream().mapToInt(Integer::intValue).toArray());

		// save back to db
		repository.save(targetNode);
		repository.save(neighborNode);

		// return a list with two elements, target and neighbor, to show both connection lists 
		// 	are updated
		ArrayList<Node> updatedNodes = new ArrayList<>();
		updatedNodes.add(targetNode);
		updatedNodes.add(neighborNode);

		return new NodeListResponseMessage("Success", "Added nodes to each others' lists", updatedNodes);
	}

	// Pull list of nodes from db, make a request to the first to pass along the message
	// If that node is unavailable, continue down list until one responds.
	@PostMapping("/api/v1/broadcast")
	public ResponseMessage broadcast(@RequestBody BroadcastMessage broadcastMessage) {
		ArrayList<Node> nodeList = repository.findAll();
		for (Node node: nodeList) {
			if (attemptBroadcast(node)) {
				return new ResponseMessage("Success", "Message broadcast to entry node " + node.getId());
			}
		}
		return new ResponseMessage("Error", "No response from nodes");
	}

	// internal method used to start a broadcast chain
	private boolean attemptBroadcast(Node node) {
		// TODO: implement the call to the node, asking to pass the message on
		return true;
	}
}