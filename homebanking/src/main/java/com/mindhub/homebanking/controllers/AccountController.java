package com.mindhub.homebanking.controllers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.AccountDTO;
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

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccount(Authentication authentication) {
        Client currentClient = clientService.findByEmail(authentication.getName());
        return currentClient.getAccounts().stream().map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        try {
            Client currentClient = clientService.findByEmail(authentication.getName());
            if (currentClient.getAccounts().size() >= 3) {
                return new ResponseEntity<>("E403 Forbidden: You have reached the limit of 3 accounts per client.", HttpStatus.FORBIDDEN);
            }
            return manageAccountCreation(accountService, currentClient);
        } catch (Exception e) {
            System.err.println("Error: An exception occurred during account creation, please contact support: " + e.getMessage());
            return new ResponseEntity<>("E403 FORBIDDEN: An exception occurred during account creation.", HttpStatus.FORBIDDEN);
        }
    }

}
