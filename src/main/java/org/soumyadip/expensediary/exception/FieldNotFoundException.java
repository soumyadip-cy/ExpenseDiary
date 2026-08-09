package org.soumyadip.expensediary.exception;

import lombok.Getter;

@Getter
public class FieldNotFoundException extends RuntimeException {

  String className;
    String fieldName;

    public FieldNotFoundException(String className, String fieldName) {
        super(String.format("Field %s not found in %s", fieldName, className));
        this.className = className;
        this.fieldName = fieldName;
    }
}
