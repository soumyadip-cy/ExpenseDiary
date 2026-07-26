package org.soumyadip.expensediary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.soumyadip.expensediary.dto.CreateMerchantRequest;
import org.soumyadip.expensediary.dto.MerchantResponse;
import org.soumyadip.expensediary.dto.UpdateMerchantRequest;
import org.soumyadip.expensediary.entity.Merchant;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE
)
public interface MerchantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "activationTime", ignore = true)
    @Mapping(target = "deactivationTime", ignore = true)
    Merchant toEntity(CreateMerchantRequest request);

    MerchantResponse toResponse(Merchant merchant);

    void updateEntity(
            UpdateMerchantRequest request,
            @MappingTarget Merchant merchant
    );
}
