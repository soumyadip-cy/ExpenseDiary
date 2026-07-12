package org.soumyadip.expensediary.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.soumyadip.expensediary.enums.AccessTimeType;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "access_times")
public class AccessTime {
    @Id
    @Column(unique = true, nullable = false, name = "id")
    protected String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    protected User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected AccessTimeType type;

    @Column(nullable = false)
    protected Instant timestamp;
}
