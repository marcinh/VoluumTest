package com.voluum.framework.exceptions;

public class LoginFailureException extends Exception {
    public LoginFailureException() {
        super("User was not logged in");
    }
}
