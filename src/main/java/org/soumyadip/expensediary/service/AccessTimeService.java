package org.soumyadip.expensediary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.AccessTimeDTO;
import org.soumyadip.expensediary.entity.*;
import org.soumyadip.expensediary.enums.AccessTimeType;
import org.soumyadip.expensediary.repository.AccessTimeRepository;
import org.soumyadip.expensediary.util.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccessTimeService {

    private final AccessTimeRepository accessTimeRepository;
    private final UlidGenerator ulidGenerator;
    private final PageableUtil pageableUtil;

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

    public Page<AccessTimeDTO> getAccessTimeList(User user, int pageNumber) {
        return getAccessTimeList(user, pageNumber, 10);
    }

    public Page<AccessTimeDTO> getAccessTimeList(User user, int pageNumber, int pageSize) {

        log.debug("Returning access time list for user: {}", user.getId());

        return accessTimeRepository.findByUser(
                user,
                pageableUtil.createPageable(pageNumber, pageSize, "timestamp")
        ).map(
                obj -> new AccessTimeDTO(
                        obj.getType(),
                        obj.getTimestamp()
                )
        );
    }
}
