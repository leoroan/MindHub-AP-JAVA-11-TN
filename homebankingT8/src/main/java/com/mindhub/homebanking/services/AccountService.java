package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccounts();

    AccountDTO getAccount(Long id);

    Account getAccount(String number);


    void saveAccount(Account account);




}
