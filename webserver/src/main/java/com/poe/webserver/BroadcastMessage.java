package com.poe.webserver;

public class BroadcastMessage {
	public String message;
	public String alertLevel; // emergency, warning, or info
	public int source; // node id

	public BroadcastMessage(String message, String alertLevel, int source) {
		this.message = message;
		this.alertLevel = alertLevel;
		this.source = source;
	}
}
