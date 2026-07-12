package org.soumyadip.expensediary.exception;

public class JwtExpireTokenCreationException extends RuntimeException {
    public JwtExpireTokenCreationException(String message) {
        super(message);
    }
}
