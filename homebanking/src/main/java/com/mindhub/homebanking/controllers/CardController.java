package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.mindhub.homebanking.utils.Utils.cardCreator;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @PostMapping ("/cards/delete")
    public ResponseEntity<Object> deleteCard(
            @RequestParam String cardNumber,
            Authentication authentication) {
        Card c = cardService.getCard(cardNumber);
        Client currentClient = clientService.findByEmail(authentication.getName());
        if (!currentClient.getCards().contains(c)) {
            return new ResponseEntity<>("E403 FORBIDDEN - CARD OWNER MISMATCH", HttpStatus.FORBIDDEN);
        }
        c.setActive(false);
        cardService.saveCard(c);
        return new ResponseEntity<>("201 ERASED", HttpStatus.CREATED);
    }


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam CardType cardType,
            @RequestParam CardColor cardColor,
            Authentication authentication) {
        Client currentClient = clientService.findByEmail(authentication.getName());

        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        // controla que el cliente NO tenga mas de 3 tarjetas activas al momento de querer pedir una nueva.
        if (currentClient.getCards().stream().filter(Card::isActive).count() > 2) {
            return new ResponseEntity<>("E403 FORBIDDEN", HttpStatus.FORBIDDEN);

        } else {

            cardCreator(cardService, currentClient, cardType, cardColor);
            return new ResponseEntity<>("201 CREATED", HttpStatus.CREATED);
        }
    }
}
