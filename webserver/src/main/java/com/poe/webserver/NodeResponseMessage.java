package com.poe.webserver;

public class NodeResponseMessage extends ResponseMessage {
    public Node payload;

    public NodeResponseMessage(String status, String message, Node payload) {
        super(status, message);
        this.payload = payload;
    }
}
