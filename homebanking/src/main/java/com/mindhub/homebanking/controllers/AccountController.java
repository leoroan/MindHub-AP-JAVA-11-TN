package com.mindhub.homebanking.controllers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.mindhub.homebanking.utils.Utils.manageAccountCreation;


@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    private ClientService getClientService() {
        return clientService;
    }

    @PostMapping("/accounts/delete") //Patch
    public ResponseEntity<Object> deleteAccount(
            @RequestParam String accountNumber,
            Authentication authentication) {

        Account a = accountService.getAccount(accountNumber);
        Client currentClient = getClientService().findByEmail(authentication.getName());

        if (!currentClient.getAccounts().contains(a) || a.getBalance() > 0 || currentClient.getAccounts().stream().filter(Account::isActive).count() == 1 ) {
            return new ResponseEntity<>("E403 FORBIDDEN - ACCOUNT OWNER MISMATCH", HttpStatus.FORBIDDEN);
        }
        a.setActive(false);
        a.getTransactions().stream().forEach(transaction -> transaction.setActive(false));
        accountService.saveAccount(a);
        return new ResponseEntity<>("201 ERASED", HttpStatus.CREATED);
    }


    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccount(Authentication authentication) {
        Client currentClient = getClientService().findByEmail(authentication.getName());
        return currentClient.getAccounts().stream().map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType accountType, Authentication authentication) {
        try {
            Client currentClient = getClientService().findByEmail(authentication.getName());
            if (currentClient.getAccounts().stream().filter(Account::isActive).count() >= 3) {
                return new ResponseEntity<>("E403 Forbidden: You have reached the limit of 3 accounts per client.", HttpStatus.FORBIDDEN);
            }
            return manageAccountCreation(accountService, currentClient, accountType);
        } catch (Exception e) {
            System.err.println("Error: An exception occurred during account creation, please contact support: " + e.getMessage());
            return new ResponseEntity<>("E403 FORBIDDEN: An exception occurred during account creation.", HttpStatus.FORBIDDEN);
        }
    }

}
