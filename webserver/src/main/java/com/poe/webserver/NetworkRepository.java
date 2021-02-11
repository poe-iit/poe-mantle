package com.poe.webserver;

import org.springframework.data.repository.CrudRepository;

public interface NetworkRepository extends CrudRepository<Node, Integer> {
  Node findById(int id);
}