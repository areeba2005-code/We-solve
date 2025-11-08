package com.wesolve.Service;

import com.wesolve.model.user;

public class userservice {

    public user loginUser(String email, String password) {
        // Dummy login data for testing
        if (email.equalsIgnoreCase("admin@gmail.com") && password.equals("1234")) {
            return new user("Admin", email);
        }
        if (email.equalsIgnoreCase("user@gmail.com") && password.equals("abcd")) {
            return new user("Maria Ali", email);
        }
        return null; // invalid login
    }

    public boolean registerUser(String name, String email, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerUser'");
    }

    public String getUserName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserName'");
    }
}
