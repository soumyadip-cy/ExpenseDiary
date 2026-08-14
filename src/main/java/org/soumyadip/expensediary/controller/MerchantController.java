package org.soumyadip.expensediary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.*;
import org.soumyadip.expensediary.mapper.PageResponseMapper;
import org.soumyadip.expensediary.service.MerchantService;
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
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    private final MerchantService merchantService;
    private final PageResponseMapper pageResponseMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MerchantResponse>>> getAll(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "name") String fieldName,
            @RequestParam(defaultValue = "desc") String sort
    ){
        Page<MerchantResponse> merchantResponses = merchantService.findAll(pageNumber, pageSize, fieldName, sort);
        PageResponse<MerchantResponse> pageResponsePage = pageResponseMapper.toPageResponse(merchantResponses);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        pageResponsePage,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @GetMapping("/{merchantId}")
    public ResponseEntity<ApiResponse<MerchantResponse>> get(
            @PathVariable String merchantId
    ) {
        MerchantResponse merchantResponse = merchantService.findById(merchantId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        merchantResponse,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<MerchantResponse>> create(
            @RequestBody
            @Valid
            CreateMerchantRequest createMerchantRequest
    ) {
        MerchantResponse merchantResponse = merchantService.createMerchant(createMerchantRequest);

        URI location = URI.create("/api/v1/merchants"+merchantResponse.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(
                        new ApiResponse<>(
                                true,
                                merchantResponse,
                                HttpStatus.CREATED.value(),
                                Instant.now()
                        )
                );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PatchMapping("/{merchantId}")
    public ResponseEntity<ApiResponse<MerchantResponse>> update(
            @PathVariable String merchantId,
            @RequestBody
            @Valid
            UpdateMerchantRequest updateMerchantRequest
    ) {
        MerchantResponse merchantResponse = merchantService.updateMerchant(merchantId, updateMerchantRequest);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        merchantResponse,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/deactivate/{merchantId}")
    public ResponseEntity<ApiResponse<ApiMessage>> deactivate(
            @PathVariable String merchantId
    ) {
        merchantService.deactivateMerchant(merchantId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        new ApiMessage("Merchant with id: "+merchantId+" has been deactivated."),
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{merchantId}")
    public ResponseEntity<ApiResponse<ApiMessage>> delete(
            @PathVariable String merchantId
    ) {
        merchantService.deleteMerchant(merchantId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        new ApiMessage("Merchant with id: "+merchantId+" has been deleted"),
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }
}
