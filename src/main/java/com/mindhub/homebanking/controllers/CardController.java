package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            Authentication authentication,
            @RequestParam CardType cardType,
            @RequestParam CardColor cardColor
    ) {
        Client client = this.clientRepository.findByEmail(authentication.getName());

        Set<Card> cards = client.getCards();
        Set<Card> creditCards = new HashSet<>();
        Set<Card> debitCards = new HashSet<>();

        for (Card card : cards) {
            if (card.getType() == CardType.CREDIT) {
                creditCards.add(card);
            } else if (card.getType()== CardType.DEBIT){
                debitCards.add(card);
            }
        }

        if (cardType == CardType.CREDIT && creditCards.size() ==3) {
            return new ResponseEntity<>("Máximo de tarjetas de crédito creadas", HttpStatus.FORBIDDEN);
        }

        if ( cardType == CardType.DEBIT && debitCards.size() ==3) {
            return new ResponseEntity<>("Máximo de tarjetas de débito creadas", HttpStatus.FORBIDDEN);
        }

        String number = String.format("%04d %04d %04d %04d",getRandomNumber(1,10000),getRandomNumber(1,10000),getRandomNumber(1,10000),getRandomNumber(1,10000));
        String cvv = String.format("%03d",getRandomNumber(1,1000));

        cardRepository.save(new Card(
                client.getFirstName() + " " + client.getLastName(),
                cardType,
                cardColor,
                number,
                cvv,
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(5),
                client));

        return new ResponseEntity<>("Tarjeta creada", HttpStatus.CREATED);

    }
}
