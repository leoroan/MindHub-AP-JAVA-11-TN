package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
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

    public Account() {
    }

    public Account(String number, LocalDate creationDate, double balance, Client client) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
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
        return id;
    }
}