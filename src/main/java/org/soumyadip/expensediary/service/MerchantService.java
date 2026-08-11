package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.*;
import org.soumyadip.expensediary.entity.Merchant;
import org.soumyadip.expensediary.exception.MerchantNotFoundException;
import org.soumyadip.expensediary.mapper.MerchantMapper;
import org.soumyadip.expensediary.repository.MerchantRepository;
import org.soumyadip.expensediary.util.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final PageableUtil pageableUtil;
    private final UlidGenerator ulidGenerator;

    public Merchant getMerchant(String id) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new MerchantNotFoundException("Merchant not found with id: " + id, id));
        log.debug("Merchant found with id: " + id);
        return merchant;
    }

    public MerchantResponse findById(String id) {

        Merchant merchant = merchantRepository.findById(id).orElseThrow(
                () -> new MerchantNotFoundException("Couldn't find merchant with the id", id)
        );
        log.info("Merchant found with id {}", id);

        MerchantResponse merchantResponse = merchantMapper.toResponse(merchant);
        log.debug("Merchant found with id {}", merchant.getId());

        return merchantResponse;
    }

    public Page<MerchantResponse> findAll(int page, int pageSize, String sortBy, String sort) {

        boolean sortByDescendingOrder = sort.equalsIgnoreCase("desc");

        Page<MerchantResponse> merchantResponses = merchantRepository.findAll(pageableUtil.createPageable(page, pageSize, sortBy, sortByDescendingOrder, Merchant.class))
                .map(merchantMapper::toResponse);
        log.debug("Merchants mapped and sending response.");

        return merchantResponses;
    }

    @Transactional
    public MerchantResponse createMerchant(CreateMerchantRequest createMerchantRequest) {

        Merchant merchant = merchantMapper.toEntity(createMerchantRequest);
        merchant.setId(ulidGenerator.generate());
        log.debug("Merchant object created with id {}", merchant.getId());
        merchantRepository.save(merchant);
        log.debug("Merchant saved with id {}", merchant.getId());
        MerchantResponse merchantResponse = merchantMapper.toResponse(merchant);
        log.debug("MerchantResponse created with id {}", merchant.getId());
        return merchantResponse;
    }

    @Transactional
    public MerchantResponse updateMerchant(String id, UpdateMerchantRequest updateMerchantRequest) {

        Merchant merchant = getMerchant(id);

        merchantMapper.updateEntity(updateMerchantRequest, merchant);
        log.info("Merchant updated with id {}", id);

        return merchantMapper.toResponse(merchant);
    }

    @Transactional
    public void deleteMerchant(String id) {

        Merchant merchant = getMerchant(id);
        merchantRepository.delete(merchant);
        log.info("Merchant deleted with id {}", id);
    }
}
