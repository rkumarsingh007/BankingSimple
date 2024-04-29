package dev.codescreen.Entity;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class TransactionAmount {
    private String amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private DebitCredit debitOrCredit;


    public TransactionAmount(String amount, String currency,DebitCredit debitOrCredit ) {
        this.debitOrCredit = debitOrCredit;
        this.currency = currency;
        this.amount = amount;
    }

    public TransactionAmount() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public DebitCredit getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(DebitCredit debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionAmount{" +
                "amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", debitOrCredit=" + debitOrCredit +
                '}';
    }
}


