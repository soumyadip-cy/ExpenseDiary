package org.soumyadip.expensediary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soumyadip.expensediary.entity.AccessTime;
import org.soumyadip.expensediary.entity.User;
import org.soumyadip.expensediary.enums.AccessTimeType;
import org.soumyadip.expensediary.repository.AccessTimeRepository;
import org.soumyadip.expensediary.util.JWTutil;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessTimeServiceTest {
    @Mock AccessTimeRepository repository;
    @Mock UlidGenerator ulidGenerator;
    @Mock JWTutil jwtutil;
    @InjectMocks AccessTimeService service;

    @Test
    void firstPageUsesZeroBasedPageZero() {
        User user = new User("id", "sam", "password", true, null);
        service.getAccessTimeList(user, 0);
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findByUserOrderByTimestampDesc(eq(user), captor.capture());
        assertEquals(0, captor.getValue().getPageNumber());
        assertEquals(10, captor.getValue().getPageSize());
    }

    @Test
    void loginAfterUnclosedSessionStoresSyntheticLogoutThenLogin() {
        User user = new User("id", "sam", "password", true, null);
        AccessTime previous = new AccessTime("old", user, AccessTimeType.LOGIN, Instant.parse("2026-01-01T00:00:00Z"));
        when(repository.findFirstByUserOrderByTimestampDesc(user)).thenReturn(Optional.of(previous));
        when(ulidGenerator.generate()).thenReturn("logout", "login");
        when(jwtutil.getExpirationTime()).thenReturn(1000L);

        service.storeAccessTime(user, AccessTimeType.LOGIN);

        ArgumentCaptor<AccessTime> captor = ArgumentCaptor.forClass(AccessTime.class);
        verify(repository, times(2)).save(captor.capture());
        assertEquals(AccessTimeType.LOGOUT, captor.getAllValues().get(0).getType());
        assertEquals(AccessTimeType.LOGIN, captor.getAllValues().get(1).getType());
    }
}
