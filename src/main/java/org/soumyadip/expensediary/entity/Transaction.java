package org.soumyadip.expensediary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String id;

    @NotBlank
    @Column(nullable = false)
    private Instant transactionDate;

    @NotNull
    @Column(nullable = false)
    private Instant creationDate;

    @NotNull
    @Column(nullable = false)
    private TransactionType transactionType;

    @NotNull
    @Column(nullable = false)
    private Beneficiary beneficiary;

    @NotNull
    @Column(nullable = false)
    private Merchant merchant;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @NotNull
    @Column(nullable = false)
    private Double amount;
}
