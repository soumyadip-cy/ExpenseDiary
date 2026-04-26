package org.soumyadip.expensediary.exception;

public class InvalidRefreshTokenException extends Exception{
    public InvalidRefreshTokenException(String message){
        super(message);
    }
}
