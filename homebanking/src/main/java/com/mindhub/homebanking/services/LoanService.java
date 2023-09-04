package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    void saveLoan(Loan loan);

    Loan findByName(String name);

    Loan findById(Long id);

    List<LoanDTO> getLoans();
}
