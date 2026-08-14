package org.soumyadip.expensediary.exception;

import lombok.Getter;

@Getter
public class TransactionTypeAlreadyExists extends RuntimeException {

    private final String id;

    public TransactionTypeAlreadyExists(String message, String transactionId) {
        super(message);
        this.id = transactionId;
    }
}
