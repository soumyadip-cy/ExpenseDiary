package org.soumyadip.expensediary.exception;

import lombok.Getter;

@Getter
public class TransactionAlreadyExists extends RuntimeException {
    private final String id;

    public TransactionAlreadyExists(String message, String transactionId) {
        super(message);
        this.id = transactionId;
    }
}
