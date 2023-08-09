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
            
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            
            Account a1 = new Account("VIN001", today, 5000);
            Account a2 = new Account("VIN002", tomorrow, 7500);

            c1.addAccount(a1);
            c1.addAccount(a2);
            
            accountRepository.save(a1);
            accountRepository.save(a2);

        };
    }

}
