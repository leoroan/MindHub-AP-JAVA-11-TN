package com.mindhub.homebanking.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;

@RestController
@RequestMapping("/api")
public class AccountController {

  @Autowired
  private AccountRepository accountRepository;

  @RequestMapping("/accounts")
  public List<Account> getAccounts() {
    return accountRepository.findAll();

  }

}
