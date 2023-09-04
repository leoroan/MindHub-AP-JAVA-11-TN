package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Random;

public class Utils {

    public static boolean validateAccountNumbers(String newNumber, AccountService accountService) {
        return (accountService.getAccount(newNumber) != null);
    }

    public static String accountNumberGenerator(AccountService accountService) {
        String prefix = "VIN-";
        int maxNum = 99999999;
        String accountNumberStr;
        do {
            int accountNumber = (int) (Math.random() * maxNum) + 1;
            accountNumberStr = String.format("%06d", accountNumber);
        } while (validateAccountNumbers(prefix + accountNumberStr, accountService));
        return prefix + accountNumberStr;
    }

    public static Account accountCreator(AccountService accountService) {
        LocalDate today = LocalDate.now();
        try {
            return new Account(accountNumberGenerator(accountService), today, 0);
        } catch (Exception e) {
            System.err.println("An exception occurred during account-number generation, please contact dev-support: " + e.getMessage());
            return null;
        }
    }

    public static ResponseEntity<Object> manageAccountCreation(AccountService accountService, Client currentClient) {
        Account createdAccount = accountCreator(accountService);
        if (createdAccount != null) {
            currentClient.addAccount(createdAccount);
            accountService.saveAccount(createdAccount);
            return new ResponseEntity<>("R201 CREATED", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("E403 FORBIDDEN: Unable to create the account.", HttpStatus.FORBIDDEN);
        }
    }

    public static int cvvGenerator() {
        Random random = new Random();
        return random.nextInt(900) + 100;
    }

    public static boolean validateCardNumbers(String newNumber, CardService cardService) {
        return (cardService.getCard(newNumber) != null);
    }

    public static String cardNumberGenerator(CardService cardService) {
        StringBuilder cardNumber = new StringBuilder();
        do {
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    cardNumber.append(random.nextInt(10));
                }
                if (i < 3) {
                    cardNumber.append("-");
                }
            }
        } while (validateCardNumbers(cardNumber.toString(), cardService));
        return cardNumber.toString();
    }

    public static void cardCreator(CardService cardService, Client currentClient, CardType type, CardColor color) {
        LocalDate from = LocalDate.now();
        LocalDate thru = from.plusDays(1825);
        Card currentCard = new Card(currentClient.getFirstName() + " " + currentClient.getLastName(), type, color,
                cardNumberGenerator(cardService), cvvGenerator(), from, thru);
        currentClient.addCard(currentCard);
        cardService.saveCard(currentCard);
    }

    public static Account getCurrentAccount(String accountNumber, Client currentClient) {
        for (Account account : currentClient.getAccounts()) {
            if (account.getNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
}
