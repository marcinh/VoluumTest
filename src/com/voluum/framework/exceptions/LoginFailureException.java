package com.voluum.framework.exceptions;

public class LoginFailureException extends Exception {
    public LoginFailureException( String reason) {
        super("User was not logged in: " + reason);
    }
}
