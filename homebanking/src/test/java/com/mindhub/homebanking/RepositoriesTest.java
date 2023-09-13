package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Test
    public void existLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));
    }

    @Test
    public void existPersonalLoan() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void nullishAccount() {
        Account a = accountRepository.findByNumber("IN-00000001");
        assertThat(a, nullValue());

    }

    @Test
    public void existAccountByNumber() {
        String accountNumber = "VIN-00000001";
        Account a = accountRepository.findAll()
                .stream()
                .filter(acc -> acc.getNumber().equals(accountNumber))
                .findAny()
                .orElse(null);
        assertThat(a, notNullValue());
    }

    @Test
    public void cardHolderExist() {
        Card c = cardRepository.findAll().stream()
                .filter(card -> card.getClient().getFirstName().equals("Melba"))
                .findAny().orElse(null);
        assertThat(Objects.requireNonNull(c).getClient().getFirstName(), equalTo("Melba"));

    }

    @Test
    public void theresNoMoreCards() {
        int totalCount = cardRepository.findAll().size();
        assertThat(totalCount, is(greaterThan(2)));
        cardRepository.deleteAll();
        totalCount = (int) cardRepository.count();
        assertThat(totalCount, is(lessThan(1)));
    }

    @Test
    public void melbaExist() {
        assertThat(clientRepository.findAll().stream().filter(client -> client.getFirstName().equals("Melba")), notNullValue());
    }

    @Test
    public void onlyFourExists() {
        assertThat(clientRepository.findAll(), hasSize(4));
    }

    @Test
    public void TheresNone() {
        Client c = clientRepository.findByEmail("mmorel@email.com");
        assertThat(c, isIn(clientRepository.findAll()));
    }


    @Test
    public void transactionExist() {
        Client c1 = new Client();
        LocalDate today = LocalDate.now();
        Account a1 = new Account("VIN-00000321", today, 0);
        c1.addAccount(a1);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = now.plusHours(1);
        Transaction t1 = new Transaction(TransactionType.CREDIT, 1000, "deposit in my own account", now);
        Transaction t2 = new Transaction(TransactionType.DEBIT, -100, "purchase in store", then);
        a1.addTransaction(t1);
        a1.addTransaction(t2);
        Long num = transactionRepository.save(t1).getId();
        transactionRepository.save(t2);

        assertThat(transactionRepository.findById(num), notNullValue());
    }


}
