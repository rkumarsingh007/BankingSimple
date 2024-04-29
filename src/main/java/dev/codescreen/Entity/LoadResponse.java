package dev.codescreen.Entity;


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Transient;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoadResponse {
    private String messageId;
    private String userId;
    private TransactionAmount balance;

    @Transient
    private String description;

    // constructor, getters and setters


    public LoadResponse(TransactionAmount balance, String userId, String messageId) {
        this.balance = balance;
        this.userId = userId;
        this.messageId = messageId;
    }

    public LoadResponse(TransactionAmount balance, String userId, String messageId, String description) {
        this.balance = balance;
        this.userId = userId;
        this.messageId = messageId;
        this.description = description;
    }

    public LoadResponse(LoadRequest loadRequest) {
        this.messageId = loadRequest.getMessageId();
        this.userId = loadRequest.getUserId();
        this.description = loadRequest.getDescription();
        this.balance = loadRequest.getTransactionAmount();
    }



    public LoadResponse() {
    }

    public TransactionAmount getBalance() {
        return balance;
    }

    public void setBalance(TransactionAmount balance) {
        this.balance = balance;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "LoadResponse{" +
                "balance=" + balance +
                ", messageId='" + messageId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
