package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Account> accounts;
    private Set<ClientLoan> loans;
    private Set<Card> cards;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts();
        this.loans = client.getClientLoans();
        this.cards = client.getCards();
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<AccountDTO> getAccounts() {
        return accounts.stream()
                .map(AccountDTO::new).collect(Collectors.toList());
    }

    public List<ClientLoanDTO> getLoans() {
        return loans.stream()
                .map(ClientLoanDTO::new).collect(Collectors.toList());
    }

    public List<CardDTO> getCards() {
        return cards.stream()
                .map(CardDTO::new).collect(Collectors.toList());
    }

}
