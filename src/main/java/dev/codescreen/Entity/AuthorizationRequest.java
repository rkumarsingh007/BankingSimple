package dev.codescreen.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AuthorizationRequest {

    @Id
    private String messageId;
    private TransactionAmount transactionAmount;
    private String userId;

    private String description;



    public AuthorizationRequest(String userId, TransactionAmount transactionAmount, String messageId) {
        this.userId = userId;
        this.transactionAmount = transactionAmount;
        this.messageId = messageId;
    }

    public AuthorizationRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public TransactionAmount getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(TransactionAmount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AuthorizationRequest{" +
                "userId='" + userId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", transactionAmount=" + transactionAmount +
                '}';
    }
}
