package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {

//    All implementations of JpaRepository include
//
//    repository.count() -- returns how many instances are in the repository
//    repository.delete(id) -- delete the instance with the given ID
//    repository.findAll() -- return a collection of all the instances
//    repository.findById(id) -- return the instance with the given ID (see below for details)
//    repository.save(instance) -- save the instance in the database
//    repository.saveAll(instances) -- save a list of instances in the database (added in Spring 2)

    // Método "findByEmail" en el repositorio de clientes que retorne un objeto de tipo Client,
    // con este método luego podré buscar un cliente por su email.
    public Client findByEmail(String email);


}
