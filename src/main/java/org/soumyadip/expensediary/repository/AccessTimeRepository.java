package org.soumyadip.expensediary.repository;

import org.soumyadip.expensediary.dto.AccessTimes;
import org.soumyadip.expensediary.entity.AccessTime;
import org.soumyadip.expensediary.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTimeRepository extends JpaRepository<AccessTime, String> {

    Page<AccessTime> findByUserOrderByTimestampDesc(User user, Pageable pageable);
    Optional<AccessTime> findFirstByUserOrderByTimestampDesc(User user);
}
