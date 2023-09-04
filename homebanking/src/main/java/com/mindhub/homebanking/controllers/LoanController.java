package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.utils.Utils.getCurrentAccount;
import static com.mindhub.homebanking.utils.Utils.manageAccountCreation;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoans();
    }

    @Transactional
    @PostMapping("/clients/current/Loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        if (loanApplicationDTO.getLoanId() == 0 || loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayments() == 0 || loanApplicationDTO.getAccountToNumber().isEmpty()) {
            return new ResponseEntity<>("E403 Missing data", HttpStatus.FORBIDDEN);
        }
        if (loanService.findById(loanApplicationDTO.getLoanId()) == null) {
            return new ResponseEntity<>("E403 non-existent loan ", HttpStatus.FORBIDDEN);
        }
        if (loanService.findById(loanApplicationDTO.getLoanId()).getMaxAmount() < loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("E403 Max-amount exceeded", HttpStatus.FORBIDDEN);
        }
        if (!loanService.findById(loanApplicationDTO.getLoanId()).getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("E403 Bad payment", HttpStatus.FORBIDDEN);
        }
        if (accountService.getAccount(loanApplicationDTO.getAccountToNumber()) == null) {
            return new ResponseEntity<>("E403 Destination account incorrect", HttpStatus.FORBIDDEN);
        }
        Client currentClient = clientService.findByEmail(authentication.getName());
        if (currentClient.getAccounts().stream().noneMatch(account -> account.getNumber().contains(loanApplicationDTO
                .getAccountToNumber()))) {
            return new ResponseEntity<>("E403 Not a client-account", HttpStatus.FORBIDDEN);
        }
        try {
            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.20, loanApplicationDTO.getPayments());
            currentClient.addLoan(clientLoan);
            Transaction transaction = new Transaction(TransactionType.CREDIT, 9999, clientLoan.getLoan().getName() + " " + "loan approved", LocalDateTime.now());
            Account currentAccount = getCurrentAccount(loanApplicationDTO.getAccountToNumber(), currentClient);
            currentAccount.addTransaction(transaction);
            accountService.getAccount(loanApplicationDTO.getAccountToNumber()).addTransaction(transaction);
            accountService.getAccount(loanApplicationDTO.getAccountToNumber()).setBalance(
                    accountService.getAccount(loanApplicationDTO.getAccountToNumber()).getBalance() + loanApplicationDTO.getAmount());
            return manageAccountCreation(accountService, currentClient);
        } catch (Exception e) {
            System.err.println("Error: An exception occurred during account creation, please contact support: " + e.getMessage());
            return new ResponseEntity<>("E403 FORBIDDEN: An exception occurred during account creation.", HttpStatus.FORBIDDEN);
        }
    }


}
