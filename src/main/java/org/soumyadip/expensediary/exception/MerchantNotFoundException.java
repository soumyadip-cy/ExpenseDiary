package org.soumyadip.expensediary.exception;

import lombok.Getter;

@Getter
public class MerchantNotFoundException extends RuntimeException {

    String id;

    public MerchantNotFoundException(String message, String id) {
        super(message);
        this.id = id;
    }
}
