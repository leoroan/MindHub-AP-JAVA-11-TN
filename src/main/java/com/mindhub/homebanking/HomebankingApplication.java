package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        return (args) -> {

            Client c1 = new Client("Melba", "Morel", "mmorel@email.com");
            Client c2 = new Client("Dama", "Bocca", "dbocca@email.com");

            clientRepository.save(c1);
            clientRepository.save(c2);

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);

            Account a1 = new Account("VIN001", today, 5000);
            Account a2 = new Account("VIN002", tomorrow, 7500);

            c1.addAccount(a1);
            c2.addAccount(a2);

            accountRepository.save(a1);
            accountRepository.save(a2);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime then = now.plusHours(1);

            Transaction t1 = new Transaction(TransactionType.CREDIT, 1000, "deposit in own account", now);
            Transaction t2 = new Transaction(TransactionType.DEBIT, -100, "purchase in store", then);

            a1.addTransaction(t1);
            a1.addTransaction(t2);


            LocalDateTime soon = now.plusHours(-2);
            LocalDateTime later = now.plusHours(5);

            Transaction t3 = new Transaction(TransactionType.CREDIT, 9999, "lottery jackpot!", soon);
            Transaction t4 = new Transaction(TransactionType.DEBIT, -9998, "just been hacked", later);

            a2.addTransaction(t3);
            a2.addTransaction(t4);

            transactionRepository.save(t1);
            transactionRepository.save(t2);
            transactionRepository.save(t3);
            transactionRepository.save(t4);

        };
    }

}
