package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.CreateTransactionTypeRequest;
import org.soumyadip.expensediary.dto.TransactionTypeResponse;
import org.soumyadip.expensediary.dto.UpdateTransactionTypeRequest;
import org.soumyadip.expensediary.entity.TransactionType;
import org.soumyadip.expensediary.exception.MerchantAlreadyExists;
import org.soumyadip.expensediary.exception.TransactionTypeNotFoundException;
import org.soumyadip.expensediary.mapper.TransactionTypeMapper;
import org.soumyadip.expensediary.repository.TransactionTypeRepository;
import org.soumyadip.expensediary.util.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TransactionTypeService {

    private final TransactionTypeRepository repository;
    private final UlidGenerator ulidGenerator;
    private final TransactionTypeMapper mapper;
    private final PageableUtil pageableUtil;

    public TransactionType getTransactionType(String id) {
        TransactionType transactionType = repository.findById(id).orElseThrow(() -> new TransactionTypeNotFoundException("TransactionType not found with id: " + id, id));
        log.debug("TransactionType found with id: {}", id);
        return transactionType;
    }

    @Transactional
    public TransactionTypeResponse createTransactionType(CreateTransactionTypeRequest typeRequest) {

        TransactionType transactionType = mapper.toEntity(typeRequest);
        transactionType.setId(ulidGenerator.generate());
        if(repository.findByName(transactionType.getName()).isPresent()) {
            throw new MerchantAlreadyExists("Transaction Type with name: " + transactionType.getName() + " already exists!", transactionType.getId());
        }
        log.debug("TransactionType creating with id: {}", transactionType.getId());
        repository.save(transactionType);
        log.info("TransactionType created with id: {}", transactionType.getId());
        TransactionTypeResponse response = mapper.toResponse(transactionType);
        log.info("Created TransactionType with id: {}", response.id());
        return response;
    }

    public Page<TransactionTypeResponse> findAll(int page, int pageSize) {

        Page<TransactionTypeResponse> responsePage = repository.findAll(pageableUtil.createPageable(page,pageSize, "name", TransactionType.class)).map(mapper::toResponse);
        log.info("Returning response page");
        return responsePage;
    }

    public TransactionTypeResponse findById(String id) {

        return mapper.toResponse(getTransactionType(id));
    }

    @Transactional
    public TransactionTypeResponse updateById(String id, UpdateTransactionTypeRequest updateRequest) {

        TransactionType transactionType = getTransactionType(id);

        mapper.updateEntity(updateRequest, transactionType);
        log.info("TransactionType updated with id: {}", id);

        return mapper.toResponse(transactionType);
    }

    @Transactional
    public void deleteById(String id) {

        TransactionType transactionType = getTransactionType(id);
        repository.delete(transactionType);
        log.info("TransactionType deleted with id: {}", id);
    }

}
