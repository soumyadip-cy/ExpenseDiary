package org.soumyadip.expensediary.mapper;

import org.mapstruct.Mapper;
import org.soumyadip.expensediary.dto.PageResponse;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = "spring"
)
public interface PageResponseMapper {

    default <T> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
