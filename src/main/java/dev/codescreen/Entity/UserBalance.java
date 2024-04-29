package dev.codescreen.Entity;

import javax.persistence.*;

@Entity
public class UserBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    private Double balance;

    private String currency;


    // constructors, getters, setters


    public UserBalance(Double balance, String userId,String currency) {
        this.balance = balance;
        this.userId = userId;
        this.currency = currency;
    }

    public UserBalance() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "UserBalance{" +
                "balance=" + balance +
                ", id=" + id +
                ", userId='" + userId + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}