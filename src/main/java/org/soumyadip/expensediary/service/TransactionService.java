package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.*;
import org.soumyadip.expensediary.entity.Beneficiary;
import org.soumyadip.expensediary.entity.Merchant;
import org.soumyadip.expensediary.entity.Transaction;
import org.soumyadip.expensediary.entity.TransactionType;
import org.soumyadip.expensediary.exception.TransactionNotFoundException;
import org.soumyadip.expensediary.mapper.TransactionMapper;
import org.soumyadip.expensediary.repository.TransactionRepository;
import org.soumyadip.expensediary.util.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final BeneficiaryService beneficiaryService;
    private final MerchantService merchantService;
    private final TransactionTypeService transactionTypeService;
    private final PageableUtil pageableUtil;
    private final UlidGenerator ulidGenerator;

    private Transaction getTransaction(String id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> new TransactionNotFoundException("Transaction not found with id: " + id, id));
        log.debug("Transaction found with id: " + id);
        return transaction;
    }

    public TransactionResponse findById(String id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> new TransactionNotFoundException("Couldn't find transaction with the id", id)
        );
        log.info("Transaction found with id {}", id);

        TransactionResponse transactionResponse = transactionMapper.toResponse(transaction);
        log.debug("Transaction found with id {}", transaction.getId());

        return transactionResponse;
    }

    public Page<TransactionResponse> findAll(int page, int pageSize, String sortBy, boolean sortByDescendingOrder) {

        Page<TransactionResponse> transactionResponses = transactionRepository.findAll(pageableUtil.createPageable(page, pageSize, sortBy, sortByDescendingOrder, Transaction.class))
                .map(transactionMapper::toResponse);
        log.debug("Transactions mapped and sending response.");

        return transactionResponses;
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest) {

        Transaction transaction = transactionMapper.toEntity(createTransactionRequest);

        Merchant merchant = merchantService.getMerchant(createTransactionRequest.merchantId());
        Beneficiary beneficiary = beneficiaryService.getBeneficiary(createTransactionRequest.beneficiaryId());
        TransactionType transactionType = transactionTypeService.getTransactionType(createTransactionRequest.transactionTypeId());

        transaction.setId(ulidGenerator.generate());
        transaction.setCreationTime(Instant.now());
        transaction.setBeneficiary(beneficiary);
        transaction.setMerchant(merchant);
        transaction.setTransactionType(transactionType);

        log.debug("Transaction object created with id {}", transaction.getId());

        transactionRepository.save(transaction);
        log.debug("Transaction saved with id {}", transaction.getId());
        TransactionResponse transactionResponse = transactionMapper.toResponse(transaction);
        log.debug("TransactionResponse created with id {}", transaction.getId());
        return transactionResponse;
    }

    @Transactional
    public TransactionResponse updateTransaction(String id, UpdateTransactionRequest updateTransactionRequest) {

        Transaction transaction = getTransaction(id);

        if(updateTransactionRequest.transactionTypeId() != null) {
            transaction.setTransactionType(
                    transactionTypeService.getTransactionType(
                            updateTransactionRequest.transactionTypeId()));
        }

        if(updateTransactionRequest.beneficiaryId() != null) {
            transaction.setBeneficiary(
                    beneficiaryService.getBeneficiary(
                            updateTransactionRequest.beneficiaryId()));
        }

        if(updateTransactionRequest.merchantId() != null) {
            transaction.setMerchant(
                    merchantService.getMerchant(
                            updateTransactionRequest.merchantId()));
        }

        transactionMapper.updateEntity(updateTransactionRequest, transaction);

        log.info("Transaction updated with id {}", id);

        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public void deleteTransaction(String id) {

        Transaction transaction = getTransaction(id);
        transactionRepository.delete(transaction);
        log.info("Transaction deleted with id {}", id);
    }
}
