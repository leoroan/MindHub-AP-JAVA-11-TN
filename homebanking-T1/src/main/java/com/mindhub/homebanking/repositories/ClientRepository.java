package com.mindhub.homebanking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mindhub.homebanking.models.Client;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {

}
