package org.soumyadip.expensediary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.soumyadip.expensediary.dto.CreateTransactionTypeRequest;
import org.soumyadip.expensediary.dto.TransactionTypeResponse;
import org.soumyadip.expensediary.dto.UpdateTransactionTypeRequest;
import org.soumyadip.expensediary.entity.TransactionType;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE
)
public interface TransactionTypeMapper {

    @Mapping(target = "id", ignore = true)
    TransactionType toEntity(CreateTransactionTypeRequest createTransactionTypeRequest);
    TransactionTypeResponse toResponse(TransactionType transactionType);

    void updateEnity(
            UpdateTransactionTypeRequest request,
            @MappingTarget TransactionType transactionType
    );
}
