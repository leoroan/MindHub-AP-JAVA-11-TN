package com.mindhub.homebanking.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mindhub.homebanking.repositories.AccountRepository;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccount(Authentication authentication) {
        Client currentClient = clientService.findByEmail(authentication.getName());
        return currentClient.getAccounts().stream().map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());

    }

    public String accountNumberGenerator() {
        String prefix = "VIN-";
        int maxNum = 99999999; // Máximo número de 8 dígitos
        int accountNumber = (int) (Math.random() * maxNum) + 1;
        String accountNumberStr = String.format("%06d", accountNumber); // Asegura que el número tenga al menos 6 dígitos
        return prefix + accountNumberStr;
    }

    public void accCreator(Client currentClient) {
        LocalDate today = LocalDate.now();
        Account currentAccount = new Account(accountNumberGenerator(), today, 0);
        currentClient.addAccount(currentAccount);
        accountService.saveAccount(currentAccount);
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client currentClient = clientService.findByEmail(authentication.getName());
        if (currentClient.getAccounts().size() >= 3) {
            return new ResponseEntity<>("E403 FORBIDDEN", HttpStatus.FORBIDDEN);
        } else {
            accCreator(currentClient);
            return new ResponseEntity<>("201 CREATED", HttpStatus.CREATED);
        }
    }

}
