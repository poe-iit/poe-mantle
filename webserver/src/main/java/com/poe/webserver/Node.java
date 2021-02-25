package com.poe.webserver;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Node {

    // human readable name describing the node, like "west hallway"
    private String name;
    private String ipAddress;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private int[] connections;

    public Node(String name, String ipAddress, int id, int[] connections) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.id = id;
        this.connections = connections;
    }

    public Node() {
        this.name = "";
        this.ipAddress = "";
        this.id = 0;
        this.connections = null;
    }

    public String getName() {
        return this.name;
    }

    public String getIPAddress() {
        return this.ipAddress;
    }

    public int getId() {
        return this.id;
    }

    public int[] getConnections() {
        return this.connections;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConnections(int[] connections) {
        this.connections = connections;
    }
}
