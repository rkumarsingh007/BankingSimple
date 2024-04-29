package dev.codescreen.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class AuthorizationResponse {

    @Id
    private String messageId;
    private String userId;
    @Enumerated(EnumType.STRING)
    private ResponseCode responseCode;
    private TransactionAmount balance;

    @Transient
    private String description;

    // constructor, getters and setters


    public AuthorizationResponse(String messageId, String userId, ResponseCode responseCode, TransactionAmount balance) {
        this.messageId = messageId;
        this.userId = userId;
        this.responseCode = responseCode;
        this.balance = balance;
    }

    public AuthorizationResponse(String messageId, String userId, ResponseCode responseCode, TransactionAmount balance, String description) {
        this.messageId = messageId;
        this.userId = userId;
        this.responseCode = responseCode;
        this.balance = balance;
        this.description = description;
    }

    public AuthorizationResponse(AuthorizationRequest authorizationRequest, ResponseCode responseCode) {
        this.messageId = authorizationRequest.getMessageId();
        this.userId = authorizationRequest.getUserId();
        this.balance = authorizationRequest.getTransactionAmount();
        this.responseCode = responseCode;
        this.description = authorizationRequest.getDescription();
    }

    public AuthorizationResponse() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public TransactionAmount getBalance() {
        return balance;
    }

    public void setBalance(TransactionAmount balance) {
        this.balance = balance;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
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
        return "AuthorizationResponse{" +
                "balance=" + balance +
                ", messageId='" + messageId + '\'' +
                ", userId='" + userId + '\'' +
                ", responseCode=" + responseCode +
                '}';
    }
}

