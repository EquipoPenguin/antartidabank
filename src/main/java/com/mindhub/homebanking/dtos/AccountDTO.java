package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class AccountDTO {

    private long id;
    Set<Transaction> transactions = new HashSet<Transaction>();
    Set<TransactionDTO> transactionsdto = new HashSet<TransactionDTO>();
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions =  account.getTransactions();
    }

    public Set<TransactionDTO> setTransactionToSetTransactionDTO(Set<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            transactionsdto.add(new TransactionDTO(transaction));
        }
        return transactionsdto;
    }

    public long getId() {
        return id;
    }

    public Set<TransactionDTO> getTransactions() {
        return setTransactionToSetTransactionDTO(this.transactions);
    }

    public void setTransaction(Set<TransactionDTO> transactionsdto) {
        this.transactionsdto = transactionsdto;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
