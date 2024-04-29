package dev.codescreen.Entity;

public class Ping {
    private String serverTime;
    private String description;

    public Ping(String description, String serverTime) {
        this.description = description;
        this.serverTime = serverTime;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Ping() {
    }


    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}