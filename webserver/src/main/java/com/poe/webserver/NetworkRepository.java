package com.poe.webserver;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

public interface NetworkRepository extends CrudRepository<Node, Integer> {
  Node findById(int id);
  ArrayList<Node> findAll();
}