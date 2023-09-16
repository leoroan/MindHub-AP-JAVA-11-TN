package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQuery(name = "account.findByNumber", query = "select c from Account c where c.number = ?1")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();
    private boolean isActive;
    private AccountType type;

    public Account() {
    }

    public Account(String number, LocalDate creationDate, double balance, boolean isActive, AccountType type) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.isActive = isActive;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    // @JsonIgnore = genera una recursividad ya que cliente tiene asociadas
    // varias cuentas
    // y a su vez cada cuenta tiene asociado al cliente esto hace que
    // el proceso de transformar los objetos Java a Json nunca termine.
    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public long getId() {
        return this.id;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        this.transactions.add(transaction);
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public AccountType getType() {
        return this.type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }
}