package org.soumyadip.expensediary.util;

import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.exception.FieldNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PageableUtil {

    private boolean checkFieldExistence(Class recordObject, String fieldName) {

        HashSet<String> fieldNames = Arrays.stream(recordObject.getDeclaredFields())
                .map(Field::toString).collect(Collectors.toCollection(HashSet::new));

        return fieldNames.contains(fieldName);
    }

    public Pageable createPageable(int pageNumber, int pageSize, String sortPropertyName, Class recordObject) {

        return createPageable(pageNumber, pageSize, sortPropertyName, false, recordObject);
    }

    public Pageable createPageable(int pageNumber, int pageSize, String sortPropertyName, boolean sortDescending, Class recordObject) {

        if(checkFieldExistence(recordObject, sortPropertyName))
            throw new FieldNotFoundException(recordObject.getName(), sortPropertyName);

        log.debug("createPageable(pageNumber={}, pageSize={})", pageNumber, pageSize);
        return PageRequest.of(
                pageNumber>0?pageNumber-1:0,
                pageSize>0?pageSize:10,
                Sort.by(sortDescending? Sort.Direction.DESC: Sort.Direction.ASC, sortPropertyName)
        );
    }
}
