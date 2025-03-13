package com.apd.ws3.auto_loan_applicationws3.Models;

public class Login {
    private String email;
    private String password;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean authenticate(String inputEmail, String inputPassword) {
        return email.equals(inputEmail) && password.equals(inputPassword);
    }
}
