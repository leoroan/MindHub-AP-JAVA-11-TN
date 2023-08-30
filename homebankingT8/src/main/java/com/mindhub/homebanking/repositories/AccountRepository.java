package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Método "findByNumber" en el repositorio de cuentas que retorne un objeto de tipo Cuenta,
    // con este método luego podré buscar una cuenta por su numero.
    public Account findByNumber(String number);

}