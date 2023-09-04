package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
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

import static com.mindhub.homebanking.utils.Utils.getCurrentAccount;
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionService.getTransactions();
    }

    private boolean checkIfDestinationAccountExist(String destinationAccount) {
        Account anAccount = accountService.getAccount(destinationAccount);
        return anAccount != null;
    }

    private boolean accountHasFunds(Client currentClient, String accountNumber, double amount) {
        Account acc = getCurrentAccount(accountNumber, currentClient);
        if (acc != null) {
            return acc.getBalance() >= amount;
        }
        return false;
    }

    private Account getDestinationAccount(String destinationAccount) {
        return accountService.getAccount(destinationAccount);
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> registerTransaction(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam double amount,
            @RequestParam String description,
            Authentication authentication) {

        Client currentClient = clientService.findByEmail(authentication.getName());

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
        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
