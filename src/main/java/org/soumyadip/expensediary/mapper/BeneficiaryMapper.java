package org.soumyadip.expensediary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.soumyadip.expensediary.dto.*;
import org.soumyadip.expensediary.entity.Beneficiary;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE
)
public interface BeneficiaryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "activationTime", ignore = true)
    @Mapping(target = "deactivationTime", ignore = true)
    Beneficiary toEntity(CreateBeneficiaryRequest request);

    BeneficiaryResponse toResponse(Beneficiary beneficiary);

    void updateEntity(
            UpdateBeneficiaryRequest request,
            @MappingTarget Beneficiary beneficiary
    );
}
