package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;

public interface CardService {

    void saveCard (Card card);

    Card getCard(String number);



}
