package org.soumyadip.expensediary.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PageableUtil {

    public Pageable createPageable(int pageNumber, int pageSize, String sortPropertyName) {

        return createPageable(pageNumber, pageSize, sortPropertyName, false);
    }

    public Pageable createPageable(int pageNumber, int pageSize, String sortPropertyName, boolean sortDescending) {

        log.debug("createPageable(pageNumber={}, pageSize={})", pageNumber, pageSize);
        return PageRequest.of(
                pageNumber>0?pageNumber-1:0,
                pageSize>0?pageSize:10,
                Sort.by(sortDescending? Sort.Direction.DESC: Sort.Direction.ASC, sortPropertyName)
        );
    }
}
