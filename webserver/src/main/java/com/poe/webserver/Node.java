package com.poe.webserver;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Node {

    // human readable name describing the node, like "west hallway"
    private String name;
    private String macAddress;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private int[] connections;

    public Node(String name, String macAddress, int id, int[] connections) {
        this.name = name;
        this.macAddress = macAddress;
        this.id = id;
        this.connections = connections;
    }

    public Node() {
        this.name = "";
        this.macAddress = "";
        this.id = 0;
        this.connections = null;
    }

    public String getName() {
        return this.name;
    }

    public String getMacAddress() {
        return this.macAddress;
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

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConnections(int[] connections) {
        this.connections = connections;
    }
}
