package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.HashSet;
import java.util.Set;
public class ClientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    Set<Account> accounts;
    Set<AccountDTO> accountsdto = new HashSet<AccountDTO>();
    Set<ClientLoan> clientLoans = new HashSet<ClientLoan>();
    Set<ClientLoanDTO> clientLoansdto = new HashSet<ClientLoanDTO>();
    Set<Card> cards = new HashSet<Card>();
    Set<CardDTO> cardsdto = new HashSet<CardDTO>();

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts =  client.getAccounts();
        this.clientLoans = client.getClientLoans();
        this.cards = client.getCards();
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<AccountDTO> setAccountsToDTO(Set<Account> accounts) {
        for (Account account : accounts) {
            accountsdto.add(new AccountDTO(account));
        }
        return accountsdto;
    }
    public Set<AccountDTO> getAccounts() {
        return setAccountsToDTO(this.accounts);
    }
    public void setAccounts(Set<AccountDTO> accountsdto) {
        this.accountsdto = accountsdto;
    }

    public Set<ClientLoanDTO> setClientLoansToDTO(Set<ClientLoan> clientLoans) {
        for (ClientLoan clientLoan : clientLoans) {
            clientLoansdto.add(new ClientLoanDTO(clientLoan));
        }
        return clientLoansdto;
    }
    public Set<ClientLoanDTO> getLoans() {
        return setClientLoansToDTO(this.clientLoans);
    }
    public void setLoans(Set<ClientLoanDTO> clientLoansdto) {
        this.clientLoansdto = clientLoansdto;
    }

    public Set<CardDTO> setCardsToDTO(Set<Card> cards) {
        for (Card card : cards) {
            cardsdto.add(new CardDTO(card));
        }
        return cardsdto;
    }
    public Set<CardDTO> getCards() {
        return setCardsToDTO(this.cards);
    }
    public void setCards(Set<CardDTO> cardsdto) {
        this.cardsdto = cardsdto;
    }

}
