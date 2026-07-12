package org.soumyadip.expensediary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddInitialUserTest {
    @Mock UserInitializerService initializer;
    @InjectMocks AddInitialUser runner;

    @Test
    void runnerInitializesAdminAndVisitor() throws Exception {
        runner.run();

        verify(initializer).CreateInitialAdmin();
        verify(initializer).CreateInitialUser();
    }
}
