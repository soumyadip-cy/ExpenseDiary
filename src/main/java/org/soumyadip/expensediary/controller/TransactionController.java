package org.soumyadip.expensediary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.*;
import org.soumyadip.expensediary.mapper.PageResponseMapper;
import org.soumyadip.expensediary.service.TransactionService;
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
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final PageResponseMapper pageResponseMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TransactionResponse>>> getAll(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "transactionTime") String fieldName,
            @RequestParam(defaultValue = "desc") String sort
    ){
        Page<TransactionResponse> transactionResponses = transactionService.findAll(pageNumber, pageSize, fieldName, sort);
        PageResponse<TransactionResponse> pageResponsePage = pageResponseMapper.toPageResponse(transactionResponses);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        pageResponsePage,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<ApiResponse<TransactionResponse>> get(
            @PathVariable String transactionId
    ) {
        TransactionResponse transactionResponse = transactionService.findById(transactionId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        transactionResponse,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> create(
            @RequestBody
            @Valid
            CreateTransactionRequest createTransactionRequest
    ) {
        TransactionResponse transactionResponse = transactionService.createTransaction(createTransactionRequest);

        URI location = URI.create("/api/v1/transactions"+transactionResponse.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(
                        new ApiResponse<>(
                                true,
                                transactionResponse,
                                HttpStatus.CREATED.value(),
                                Instant.now()
                        )
                );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PatchMapping("/{transactionId}")
    public ResponseEntity<ApiResponse<TransactionResponse>> update(
            @PathVariable String transactionId,
            @RequestBody
            @Valid
            UpdateTransactionRequest updateTransactionRequest
    ) {
        TransactionResponse transactionResponse = transactionService.updateTransaction(transactionId, updateTransactionRequest);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        transactionResponse,
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<ApiResponse<ApiMessage>> delete(
            @PathVariable String transactionId
    ) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        new ApiMessage("Transaction with id: "+transactionId+" has been deleted"),
                        HttpStatus.OK.value(),
                        Instant.now()
                )
        );
    }
}
