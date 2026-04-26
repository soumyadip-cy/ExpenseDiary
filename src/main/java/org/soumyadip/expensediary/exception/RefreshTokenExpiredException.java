package org.soumyadip.expensediary.exception;

public class RefreshTokenExpiredException extends Exception{
    public RefreshTokenExpiredException(String message){
        super(message);
    }
}
