package org.soumyadip.expensediary.exception;

import lombok.Getter;

@Getter
public class MerchantAlreadyExists extends RuntimeException {
    private final String id;

    public MerchantAlreadyExists(String message, String transactionId) {
        super(message);
        this.id = transactionId;
    }
}
