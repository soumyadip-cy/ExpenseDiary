package org.soumyadip.expensediary.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class TransactionTypeNotFoundException extends RuntimeException {

    private final String id;

    public TransactionTypeNotFoundException(String message, String transactionId) {
        super(message);
        this.id = transactionId;
    }
}
