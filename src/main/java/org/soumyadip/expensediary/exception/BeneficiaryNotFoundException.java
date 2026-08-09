package org.soumyadip.expensediary.exception;

import lombok.Getter;

@Getter
public class BeneficiaryNotFoundException extends RuntimeException {

    String id ;

    public BeneficiaryNotFoundException(String message, String id) {
        super(message);
        this.id = id;
    }
}
