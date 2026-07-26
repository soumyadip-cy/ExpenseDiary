package org.soumyadip.expensediary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.*;
import org.soumyadip.expensediary.mapper.PageResponseMapper;
import org.soumyadip.expensediary.service.TransactionTypeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction-type")
public class TransactionTypeController {

    private final TransactionTypeService transactionTypeService;
    private final PageResponseMapper pageResponseMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TransactionTypeResponse>>> getAll(
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    ){
        Page<TransactionTypeResponse> transactionTypeResponsePage = transactionTypeService.findAll(pageNumber, pageSize);
        PageResponse<TransactionTypeResponse> pageResponsePage = pageResponseMapper.toPageResponse(transactionTypeResponsePage);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        pageResponsePage,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<ApiResponse<TransactionTypeResponse>> get(
            @PathVariable String typeId
    ) {
        TransactionTypeResponse transactionTypeResponse = transactionTypeService.findById(typeId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        transactionTypeResponse,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<TransactionTypeResponse>> create(
            @RequestBody
            @Valid
            CreateTransactionTypeRequest createTransactionTypeRequest
    ) {
        TransactionTypeResponse transactionTypeResponse = transactionTypeService.createTransactionType(createTransactionTypeRequest);

        URI location = URI.create("/api/v1/transaction-type"+transactionTypeResponse.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(
                        new ApiResponse<>(
                        true,
                        transactionTypeResponse,
                        HttpStatus.CREATED.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PatchMapping("/{typeId}")
    public ResponseEntity<ApiResponse<TransactionTypeResponse>> update(
            @PathVariable String typeId,
            @RequestBody
            @Valid
            UpdateTransactionTypeRequest updateTransactionTypeRequest
    ) {
        TransactionTypeResponse transactionTypeResponse = transactionTypeService.updateById(typeId, updateTransactionTypeRequest);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        transactionTypeResponse,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{typeId}")
    public ResponseEntity<ApiResponse<ApiMessage>> delete(
            @PathVariable String typeId
    ) {
        transactionTypeService.deleteById(typeId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        new ApiMessage("Transaction Type with id: "+typeId+" has been deleted"),
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

}
