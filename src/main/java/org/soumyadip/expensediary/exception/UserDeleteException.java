package org.soumyadip.expensediary.exception;

public class UserDeleteException extends RuntimeException {
    public UserDeleteException(String message) {
        super(message);
    }
}
