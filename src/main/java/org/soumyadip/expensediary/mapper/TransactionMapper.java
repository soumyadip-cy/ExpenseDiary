package org.soumyadip.expensediary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.soumyadip.expensediary.dto.CreateTransactionRequest;
import org.soumyadip.expensediary.dto.TransactionResponse;
import org.soumyadip.expensediary.dto.UpdateTransactionRequest;
import org.soumyadip.expensediary.entity.Transaction;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE
)
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "transactionType", ignore = true)
    @Mapping(target = "beneficiary", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    Transaction toEntity(CreateTransactionRequest request);

    @Mapping(target = "transactionTypeName", source = "transactionType.name")
    @Mapping(target = "transactionTypeId", source = "transactionType.id")
    @Mapping(target = "beneficiaryName", source = "beneficiary.name")
    @Mapping(target = "beneficiaryId", source = "beneficiary.id")
    @Mapping(target = "merchantName", source = "merchant.name")
    @Mapping(target = "merchantId", source = "merchant.id")
    TransactionResponse toResponse(Transaction transaction);

    void updateEntity(
            UpdateTransactionRequest request,
            @MappingTarget Transaction transaction
    );
}
