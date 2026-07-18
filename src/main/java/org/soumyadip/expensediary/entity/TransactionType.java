package org.soumyadip.expensediary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "transaction_types")
public class TransactionType {

    @NonNull
    @Id
    private String id;

    @NonNull
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;
}
