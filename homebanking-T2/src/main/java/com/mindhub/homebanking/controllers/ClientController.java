package com.mindhub.homebanking.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;

@RestController
@RequestMapping("/api")
public class ClientController {

  @Autowired
  private ClientRepository clientRepository;

  @RequestMapping("/clients")
  public List<Client> getClients() {
    return clientRepository.findAll();

  }

}
