package org.soumyadip.expensediary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.*;
import org.soumyadip.expensediary.mapper.PageResponseMapper;
import org.soumyadip.expensediary.service.BeneficiaryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;
    private final PageResponseMapper pageResponseMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BeneficiaryResponse>>> getAll(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "name") String fieldName,
            @RequestParam(defaultValue = "desc") String sort
    ){
        Page<BeneficiaryResponse> beneficiaryResponses = beneficiaryService.findAll(pageNumber, pageSize, fieldName, sort);
        PageResponse<BeneficiaryResponse> pageResponsePage = pageResponseMapper.toPageResponse(beneficiaryResponses);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        pageResponsePage,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @GetMapping("/{beneficiaryId}")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> get(
            @PathVariable String beneficiaryId
    ) {
        BeneficiaryResponse beneficiaryResponse = beneficiaryService.findById(beneficiaryId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        beneficiaryResponse,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> create(
            @RequestBody
            @Valid
            CreateBeneficiaryRequest createBeneficiaryRequest
    ) {
        BeneficiaryResponse beneficiaryResponse = beneficiaryService.createBeneficiary(createBeneficiaryRequest);

        URI location = URI.create("/api/v1/beneficiaries"+beneficiaryResponse.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(
                        new ApiResponse<>(
                                true,
                                beneficiaryResponse,
                                HttpStatus.CREATED.value(),
                                Instant.now()
                        )
                );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PatchMapping("/{beneficiaryId}")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> update(
            @PathVariable String beneficiaryId,
            @RequestBody
            @Valid
            UpdateBeneficiaryRequest updateBeneficiaryRequest
    ) {
        BeneficiaryResponse beneficiaryResponse = beneficiaryService.updateBeneficiary(beneficiaryId, updateBeneficiaryRequest);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        beneficiaryResponse,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/deactivate/{beneficiaryId}")
    public ResponseEntity<ApiResponse<ApiMessage>> deactivate(
            @PathVariable String beneficiaryId
    ) {
        beneficiaryService.deactivateBeneficiary(beneficiaryId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        new ApiMessage("Beneficiary with id: "+beneficiaryId+" has been deactivated."),
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{beneficiaryId}")
    public ResponseEntity<ApiResponse<ApiMessage>> delete(
            @PathVariable String beneficiaryId
    ) {
        beneficiaryService.deleteBeneficiary(beneficiaryId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        new ApiMessage("Beneficiary with id: "+beneficiaryId+" has been deleted"),
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }
}
