package org.soumyadip.expensediary.exception;

public class UsernameAlreadyExists extends RuntimeException{

    public UsernameAlreadyExists(String message){
        super(message);
    }
}
