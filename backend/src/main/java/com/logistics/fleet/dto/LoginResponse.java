package com.logistics.fleet.dto;

public class LoginResponse {

    private String token;
    private String tokenType;
    private String email;
    private String fullName;
    private String role;

    public LoginResponse() {
        this.tokenType = "Bearer";
    }

    public LoginResponse(String token, String email, String role) {
        this.token = token;
        this.tokenType = "Bearer";
        this.email = email;
        this.fullName = email;
        this.role = role;
    }

    public LoginResponse(String token, String email, String fullName, String role) {
        this.token = token;
        this.tokenType = "Bearer";
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
