package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;

public class ClientDTO {

  private long id;
  private String firstName;
  private String lastName;
  private String email;

  public ClientDTO(Client client) {

    this.id = client.getId();

    this.firstName = client.getFirstName();

    this.lastName = client.getLastName();

    this.email = client.getEmail();

  }

}
