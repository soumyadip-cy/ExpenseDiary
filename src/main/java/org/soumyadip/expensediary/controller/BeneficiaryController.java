package org.soumyadip.expensediary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.entity.Beneficiary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {

//    @GetMapping
//    public ResponseEntity<List<Beneficiary>> getAllBeneficiaries() {
//
//    }
}
