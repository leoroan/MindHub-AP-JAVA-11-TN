package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionRepository.findAll().stream()
                .map(transaction -> new TransactionDTO(transaction))
                .collect(toList());
    }

    public Account getCurrentAccount(String accountNumber, Client currentClient) {
        for (Account account : currentClient.getAccounts()) {
            if (account.getNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    private boolean checkIfDestinationAccountExist(String destinationAccount) {
        Account anAccount = accountRepository.findByNumber(destinationAccount);
        return anAccount != null;
    }

    private boolean accountHasFunds(Client currentClient, String accountNumber, double amount) {
        return getCurrentAccount(accountNumber, currentClient).getBalance() >= amount;
    }

    private Account getDestinationAccount(String destinationAccount) {
        return accountRepository.findByNumber(destinationAccount);
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> registerTransaction(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam double amount,
            @RequestParam String description,
            Authentication authentication) {

        Client currentClient = clientRepository.findByEmail(authentication.getName());

        System.out.println("Transaction registration started.");

        if (amount <= 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.equals(toAccountNumber) || getCurrentAccount(fromAccountNumber, currentClient) == null
                || !checkIfDestinationAccountExist(toAccountNumber)) {
            return new ResponseEntity<>("Transaction error", HttpStatus.FORBIDDEN);
        }
        if (!accountHasFunds(currentClient, fromAccountNumber, amount)) {
            return new ResponseEntity<>("Transaction error : no funds", HttpStatus.FORBIDDEN);
        }

        LocalDateTime now = LocalDateTime.now();
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, -amount,
                description + " " + fromAccountNumber, now);
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount,
                description + " " + toAccountNumber, now);
        // 1st debit
        getCurrentAccount(fromAccountNumber, currentClient).addTransaction(debitTransaction);
        getCurrentAccount(fromAccountNumber, currentClient)
                .setBalance(getCurrentAccount(fromAccountNumber, currentClient).getBalance() - amount);
        // then credit
        getDestinationAccount(toAccountNumber).addTransaction(creditTransaction);
        getDestinationAccount(toAccountNumber).setBalance(getDestinationAccount(toAccountNumber).getBalance() + amount);
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
