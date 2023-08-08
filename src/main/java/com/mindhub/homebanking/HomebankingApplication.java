package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    Client c1 = new Client("Melba", "Morel", "mmorel@email.com");
    Client c2 = new Client("Dama", "Bocca", "dbocca@email.com");

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
        return (args) -> {
            clientRepository.save(c1);
            clientRepository.save(c2);
            LocalDate hoy = LocalDate.now();
            LocalDate maniana = hoy.plusDays(1);
            accountRepository.save(new Account("VIN001", hoy, 5000, c1));
            accountRepository.save(new Account("VIN002", maniana, 7500, c2));

        };
    }

}
