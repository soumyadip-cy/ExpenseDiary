package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.CreateTransactionTypeRequest;
import org.soumyadip.expensediary.entity.TransactionType;
import org.soumyadip.expensediary.repository.TransactionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionTypeService {

    private final TransactionTypeRepository transactionTypeRepository;
    private final UlidGenerator ulidGenerator;

    public void newTransactionType(CreateTransactionTypeRequest typeRequest) {

        TransactionType transactionType = new TransactionType(
                ulidGenerator.generate(),
                typeRequest.name(),
                typeRequest.description()
        );

        transactionTypeRepository.save(transactionType);
    }

    public HashSet<TransactionType> findAll() {
        return transactionTypeRepository.findAllAsSet();
    }

    public TransactionType findById(String id) {
        return transactionTypeRepository.findById(id).orElse(null);
    }

    public void updateById(String id, CreateTransactionTypeRequest typeRequest) {
        TransactionType transactionType = findById(id);
        transactionType.setName(typeRequest.name());
        transactionType.setDescription(typeRequest.description());
    }

    public void deleteById(String id) {
        transactionTypeRepository.deleteById(id);
    }

}
