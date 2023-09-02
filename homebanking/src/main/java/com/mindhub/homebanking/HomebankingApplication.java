package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    /**
     * Anota la propiedad (passwordEncoder) con @Autowired para que Spring inyecte
     * el objeto PasswordEncoder
     * que se crea con el @Bean en la clase WebAuthentication
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            LoanRepository loanRepository,
            ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {

        return (args) -> {

            Client c1 = new Client("Melba", "Morel", "mmorel@email.com", passwordEncoder.encode("123456"));
            Client c2 = new Client("Dama", "Bocca", "dbocca@email.com", passwordEncoder.encode("654321"));
            Client admin = new Client("ADMIN", "ADMIN", "admin@admin.com", passwordEncoder.encode("123456"));

            clientRepository.save(c1);
            clientRepository.save(c2);
            clientRepository.save(admin);

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);

            Account a1 = new Account("VIN-00000001", today, 5000);
            Account a2 = new Account("VIN-00000002", tomorrow, 7500);
            Account a3 = new Account("VIN-00000003", today, 999);

            c1.addAccount(a1);
            c2.addAccount(a2);
            admin.addAccount(a3);

            accountRepository.save(a1);
            accountRepository.save(a2);
            accountRepository.save(a3);

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

            Loan l1 = new Loan("Hipotecario", 500000.00, List.of(12, 24, 36, 48, 60));
            Loan l2 = new Loan("Personal", 100000.00, List.of(6, 12, 24));
            Loan l3 = new Loan("Automotriz", 300000.00, List.of(6, 12, 24, 36));

            loanRepository.save(l1);
            loanRepository.save(l2);
            loanRepository.save(l3);

            ClientLoan cl1 = new ClientLoan(400000, 60);
            c1.addLoan(cl1); // reparar esto segun lo comentado en ws. faltaria el add en ClientLoan
            l1.addClient(cl1);

            ClientLoan cl2 = new ClientLoan(50000, 12);
            c1.addLoan(cl2);
            l2.addClient(cl2);

            ClientLoan cl3 = new ClientLoan(1009000, 24);
            c2.addLoan(cl3);
            l2.addClient(cl3);

            ClientLoan cl4 = new ClientLoan(200000, 36);
            c2.addLoan(cl4);
            l3.addClient(cl4);

            clientLoanRepository.save(cl1);
            clientLoanRepository.save(cl2);
            clientLoanRepository.save(cl3);
            clientLoanRepository.save(cl4);

            LocalDate from = LocalDate.now();
            LocalDate thru = from.plusDays(730);
            Card goldCard = new Card(c1.getFirstName() + " " + c1.getLastName(), CardType.DEBIT, CardColor.GOLD,
                    "3333-1212-5432-4443", 987, from, thru);
            Card titaniumCard = new Card(c1.getFirstName() + " " + c1.getLastName(), CardType.CREDIT,
                    CardColor.TITANIUM, "4443-1212-5432-3333", 652, from, thru);
            Card silverCard = new Card(c2.getFirstName() + " " + c2.getLastName(), CardType.CREDIT, CardColor.SILVER,
                    "5432-3333-4443-1212", 258, from, thru);
            c1.addCard(goldCard);
            c1.addCard(titaniumCard);
            c2.addCard(silverCard);

            cardRepository.save(goldCard);
            cardRepository.save(silverCard);
            cardRepository.save(titaniumCard);

        };
    }

}
