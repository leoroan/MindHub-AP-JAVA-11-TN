package com.mindhub.homebanking;

import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {

    @Autowired
    CardService cardService;

    @Test
    public void cardNumberIsCreated() {
        String cardNumber = Utils.cardNumberGenerator(cardService);
        assertThat(cardNumber, is(not(emptyOrNullString())));

    }
}
