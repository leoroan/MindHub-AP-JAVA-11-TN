package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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

    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(Authentication authentication) {
        Client currentClient = clientService.findByEmail(authentication.getName());
        if (currentClient.getFirstName().equals("ADMIN")) {
            return loanService.getLoansSpecial();
        }
        return loanService.getLoans();
    }

//    @Transactional
//    @PostMapping("/loans")
//    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
//
//        if (loanApplicationDTO.getLoanId() == 0 || loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayments() == 0 || loanApplicationDTO.getToAccountNumber().isEmpty()) {
//            return new ResponseEntity<>("E403 Missing data", HttpStatus.FORBIDDEN);
//        }
//        if (loanService.findById(loanApplicationDTO.getLoanId()) == null) {
//            return new ResponseEntity<>("E403 non-existent loan ", HttpStatus.FORBIDDEN);
//        }
//        if (loanService.findById(loanApplicationDTO.getLoanId()).getMaxAmount() < loanApplicationDTO.getAmount()) {
//            return new ResponseEntity<>("E403 Max-amount exceeded", HttpStatus.FORBIDDEN);
//        }
//        if (!loanService.findById(loanApplicationDTO.getLoanId()).getPayments().contains(loanApplicationDTO.getPayments())) {
//            return new ResponseEntity<>("E403 Bad payment", HttpStatus.FORBIDDEN);
//        }
//        if (accountService.getAccount(loanApplicationDTO.getToAccountNumber()) == null) {
//            return new ResponseEntity<>("E403 Destination account incorrect", HttpStatus.FORBIDDEN);
//        }
//        Client currentClient = clientService.findByEmail(authentication.getName());
//        if (currentClient.getAccounts().stream().noneMatch(account -> account.getNumber().contains(loanApplicationDTO
//                .getToAccountNumber()))) {
//            return new ResponseEntity<>("E403 Not a client-account", HttpStatus.FORBIDDEN);
//        }
//        try {
//            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.20, loanApplicationDTO.getPayments());
//            currentClient.addLoan(clientLoan);
//            Transaction transaction = new Transaction(TransactionType.CREDIT, 9999, loanApplicationDTO.getLoanId() + " " + "loan approved", LocalDateTime.now());
//            Account currentAccount = getCurrentAccount(loanApplicationDTO.getToAccountNumber(), currentClient);
//            currentAccount.addTransaction(transaction);
//            accountService.getAccount(loanApplicationDTO.getToAccountNumber()).addTransaction(transaction);
//            accountService.getAccount(loanApplicationDTO.getToAccountNumber()).setBalance(
//                    accountService.getAccount(loanApplicationDTO.getToAccountNumber()).getBalance() + loanApplicationDTO.getAmount());
//            return manageAccountCreation(accountService, currentClient);
//        } catch (Exception e) {
//            System.err.println("Error: An exception occurred during account creation, please contact support: " + e.getMessage());
//            return new ResponseEntity<>("E403 FORBIDDEN: An exception occurred during account creation.", HttpStatus.FORBIDDEN);
//        }
//    }

    private boolean isInvalidLoanApplication(LoanApplicationDTO loanApplicationDTO) {
        return loanApplicationDTO.getLoanId() == 0 ||
                loanApplicationDTO.getAmount() == 0 ||
                loanApplicationDTO.getPayments() == 0 ||
                loanApplicationDTO.getToAccountNumber().isEmpty();
    }

    private boolean isClientAccount(Client client, String accountNumber) {
        return client.getAccounts().stream().anyMatch(account -> account.getNumber().contains(accountNumber));
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        if (isInvalidLoanApplication(loanApplicationDTO)) {
            return new ResponseEntity<>("E403 Missing or invalid data", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.findById(loanApplicationDTO.getLoanId());
        if (loan == null) {
            return new ResponseEntity<>("E403 Non-existent loan", HttpStatus.FORBIDDEN);
        }

        if (loan.getMaxAmount() < loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("E403 Max-amount exceeded", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("E403 Bad payment", HttpStatus.FORBIDDEN);
        }

        Account destinationAccount = accountService.getAccount(loanApplicationDTO.getToAccountNumber());
        if (destinationAccount == null) {
            return new ResponseEntity<>("E403 Destination account incorrect", HttpStatus.FORBIDDEN);
        }

        Client currentClient = clientService.findByEmail(authentication.getName());
        if (!isClientAccount(currentClient, loanApplicationDTO.getToAccountNumber())) {
            return new ResponseEntity<>("E403 Not a client-account", HttpStatus.FORBIDDEN);
        }

        try {
            double loanAmountWithInterest = loanApplicationDTO.getAmount() * 1.20;
            ClientLoan clientLoan = new ClientLoan(loanAmountWithInterest, loanApplicationDTO.getPayments());
            currentClient.addLoan(clientLoan);
            Loan theLoan = loanService.findById(loanApplicationDTO.getLoanId());
            theLoan.addClient(clientLoan);

            Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loanApplicationDTO.getLoanId() + " loan approved", LocalDateTime.now(), true);

            Account currentAccount = getCurrentAccount(loanApplicationDTO.getToAccountNumber(), currentClient);

            assert currentAccount != null;
            currentAccount.addTransaction(transaction);
            destinationAccount.addTransaction(transaction);

            double newBalance = destinationAccount.getBalance() + loanApplicationDTO.getAmount();
            destinationAccount.setBalance(newBalance);

            clientLoanRepository.save(clientLoan);
            transactionRepository.save(transaction);

//            return manageAccountCreation(accountService, currentClient,AccountType.CHECKING_ACCOUNT);
            return new ResponseEntity<>("E201 CREATED", HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Error: An exception occurred during account creation, please contact support: " + e.getMessage());
            return new ResponseEntity<>("E403 FORBIDDEN: An exception occurred during account creation.", HttpStatus.FORBIDDEN);
        }
    }
}
