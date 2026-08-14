package org.soumyadip.expensediary.exception;

import lombok.Getter;

@Getter
public class BeneficiaryAlreadyExists extends RuntimeException {
    private final String id;

    public BeneficiaryAlreadyExists(String message, String transactionId) {
        super(message);
        this.id = transactionId;
    }
}
