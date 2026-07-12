package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.entity.*;
import org.soumyadip.expensediary.enums.AccessTimeType;
import org.soumyadip.expensediary.repository.AccessTimeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccessTimeService {

    private final AccessTimeRepository accessTimeRepository;
    private final UlidGenerator ulidGenerator;

    public void storeAccessTime(User user, AccessTimeType accessTimeType) {

        AccessTime accessTime = new AccessTime(
                ulidGenerator.generate(),
                user,
                accessTimeType,
                Instant.now()
        );

        accessTimeRepository.save(accessTime);
        log.info("User {}'s {} time has been stored in database.", user.getId(), accessTimeType.name());
    }

    public Page<AccessTime> getAccessTimeList(User user, int pageNumber) {
        return getAccessTimeList(user, pageNumber, 10);
    }

    public Page<AccessTime> getAccessTimeList(User user, int pageNumber, int pageSize) {

        int zeroBasedPage = Math.max(pageNumber - 1, 0);
        int effectivePageSize = pageSize > 0 ? pageSize : 10;
        PageRequest pageRequest  = PageRequest.of(zeroBasedPage, effectivePageSize);

        log.info("Returning access time list for user: "+user.getId());

        return accessTimeRepository.findByUserOrderByTimestampDesc(user, pageRequest);
    }
}
