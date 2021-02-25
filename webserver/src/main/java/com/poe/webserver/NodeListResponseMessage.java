package com.poe.webserver;

import java.util.ArrayList;

public class NodeListResponseMessage extends ResponseMessage {
    public ArrayList<Node> payload;

    public NodeListResponseMessage(String status, String message, ArrayList<Node> payload) {
        super(status, message);
        this.payload = payload;
    }
}
