package com.example.FYPPasswordManager;

public class ProfileModel {

    private int id;
    private String username;
    private String hash;

    //constructor
    public ProfileModel(int id, String username, String hash) {
        this.id = id;
        this.username = username;
        this.hash = hash;
    }

    //toString
    @Override
    public String toString() {
        return "ProfileModel{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
