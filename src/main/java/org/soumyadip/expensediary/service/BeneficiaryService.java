package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.BeneficiaryResponse;
import org.soumyadip.expensediary.dto.CreateBeneficiaryRequest;
import org.soumyadip.expensediary.dto.UpdateBeneficiaryRequest;
import org.soumyadip.expensediary.entity.Beneficiary;
import org.soumyadip.expensediary.entity.Merchant;
import org.soumyadip.expensediary.exception.BeneficiaryAlreadyExists;
import org.soumyadip.expensediary.exception.BeneficiaryNotFoundException;
import org.soumyadip.expensediary.exception.MerchantAlreadyExists;
import org.soumyadip.expensediary.mapper.BeneficiaryMapper;
import org.soumyadip.expensediary.repository.BeneficiaryRepository;
import org.soumyadip.expensediary.util.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Slf4j
@RequiredArgsConstructor
@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final BeneficiaryMapper beneficiaryMapper;
    private final PageableUtil pageableUtil;
    private final UlidGenerator ulidGenerator;

    public Beneficiary getBeneficiary(String id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id).orElseThrow(() -> new BeneficiaryNotFoundException("Beneficiary not found with id: " + id, id));
        log.debug("Beneficiary found with id: " + id);
        return beneficiary;
    }

    public BeneficiaryResponse findById(String id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id).orElseThrow(
                () -> new BeneficiaryNotFoundException("Couldn't find beneficiary with the id", id)
        );

        log.info("Beneficiary found with id {}", id);

        BeneficiaryResponse beneficiaryResponse = beneficiaryMapper.toResponse(beneficiary);

        log.debug("Beneficiary found with id {}", beneficiary.getId());

        return beneficiaryResponse;
    }

    public Page<BeneficiaryResponse> findAll(int page, int pageSize, String sortBy, String sort) {

        boolean sortByDescendingOrder = sort.equalsIgnoreCase("desc");

        Page<BeneficiaryResponse> beneficiaryResponses = beneficiaryRepository.findAll(pageableUtil.createPageable(page, pageSize, sortBy, sortByDescendingOrder, Beneficiary.class))
                .map(beneficiaryMapper::toResponse);

        log.debug("Beneficiaries mapped and sending response.");

        return beneficiaryResponses;
    }

    @Transactional
    public BeneficiaryResponse createBeneficiary(CreateBeneficiaryRequest createBeneficiaryRequest) {

        Beneficiary beneficiary = beneficiaryMapper.toEntity(createBeneficiaryRequest);
        beneficiary.setId(ulidGenerator.generate());
        beneficiary.setActive(true);
        beneficiary.setActivationTime(Instant.now());
        if(beneficiaryRepository.findByName(beneficiary.getName()).isPresent()) {
            throw new BeneficiaryAlreadyExists("Beneficiary with name: " + beneficiary.getName() + " already exists!", beneficiary.getId());
        }
        log.debug("Beneficiary object created with id {}", beneficiary.getId());
        beneficiaryRepository.save(beneficiary);
        log.debug("Beneficiary saved with id {}", beneficiary.getId());
        BeneficiaryResponse beneficiaryResponse = beneficiaryMapper.toResponse(beneficiary);
        log.debug("BeneficiaryResponse created with id {}", beneficiary.getId());
        return beneficiaryResponse;
    }

    @Transactional
    public BeneficiaryResponse updateBeneficiary(String id, UpdateBeneficiaryRequest updateBeneficiaryRequest) {

        Beneficiary beneficiary = getBeneficiary(id);

        beneficiaryMapper.updateEntity(updateBeneficiaryRequest, beneficiary);
        log.info("Beneficiary updated with id {}", id);

        return beneficiaryMapper.toResponse(beneficiary);
    }

    @Transactional
    public void deactivateBeneficiary(String id) {

        Beneficiary beneficiary = getBeneficiary(id);
        beneficiary.setActive(false);
        beneficiary.setDeactivationTime(Instant.now());
        log.info("Beneficiary deactivated with id {}", id);
    }

    @Transactional
    public void deleteBeneficiary(String id) {

        Beneficiary beneficiary = getBeneficiary(id);
        beneficiaryRepository.delete(beneficiary);
        log.info("Beneficiary deleted with id {}", id);
    }
}
