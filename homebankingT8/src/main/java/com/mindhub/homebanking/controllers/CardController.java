package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    public static int cvvGenerator() {
        Random random = new Random();
        return random.nextInt(900) + 100;
    }

    public static String cardNumberGenerator() {
        StringBuilder cardNumber = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardNumber.append(random.nextInt(10));
            }
            if (i < 3) {
                cardNumber.append("-");
            }
        }
        return cardNumber.toString();
    }

    public void cardCreator(Client currentClient, CardType type, CardColor color) {
        LocalDate from = LocalDate.now();
        LocalDate thru = from.plusDays(1825);
        Card currentCard = new Card(currentClient.getFirstName() + " " + currentClient.getLastName(), type, color,
                cardNumberGenerator(), cvvGenerator(), from, thru);
        currentClient.addCard(currentCard);
        cardService.saveCard(currentCard);
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(
            @RequestParam CardType cardType,
            @RequestParam CardColor cardColor,
            Authentication authentication) {
        Client currentClient = clientService.findByEmail(authentication.getName());
        if (currentClient.getCards().size() > 2) {
            return new ResponseEntity<>("E403 FORBIDDEN", HttpStatus.FORBIDDEN);
        } else {
            cardCreator(currentClient, cardType, cardColor);
            return new ResponseEntity<>("201 CREATED", HttpStatus.CREATED);
        }


    }


}
