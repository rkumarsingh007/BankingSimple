package dev.codescreen.Entity;

public class LoadRequest {
    private String userId;
    private String messageId;
    private TransactionAmount transactionAmount;

    private String description;

    // getters and setters


    public LoadRequest(String messageId, String userId, TransactionAmount transactionAmount) {
        this.messageId = messageId;
        this.userId = userId;
        this.transactionAmount = transactionAmount;
    }

    public LoadRequest() {
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
        return "LoadRequest{" +
                "messageId='" + messageId + '\'' +
                ", userId='" + userId + '\'' +
                ", transactionAmount=" + transactionAmount +
                '}';
    }
}
