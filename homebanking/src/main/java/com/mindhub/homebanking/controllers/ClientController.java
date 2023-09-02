package com.mindhub.homebanking.controllers;

import java.util.List;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import static com.mindhub.homebanking.utils.Utils.accountCreator;
import static com.mindhub.homebanking.utils.Utils.manageAccountCreation;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;


    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClients();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        // return clientRepository.findById(id).orElse(null);
        return clientService.getClient(id);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("E403 Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("E403 Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName.toUpperCase(), lastName.toUpperCase(), email, passwordEncoder.encode(password));
        clientService.saveClient(client);
        return manageAccountCreation(accountService, client);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        Client cli = clientService.findByEmail(authentication.getName());
        return new ClientDTO(cli);
    }
}
