package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Card> cards = new HashSet<>();

    public Client() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return firstName.equals(client.firstName) && lastName.equals(client.lastName) && email.equals(client.email);
    }

    public Client(String first, String last, String email, String password) {
        this.id= id;
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.password = password;
    }

    public String toString() {
        return firstName + " " + lastName;
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
    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccount(Set<Account> account) {
        this.accounts = accounts;
    }

    public List<Loan> getLoans() {
        return clientLoans.stream().map(sub -> sub.getLoan()).collect(toList());
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoan(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCard(Set<Card> card) {
        this.cards = card;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

}

