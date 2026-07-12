package org.soumyadip.expensediary.dto;

import org.soumyadip.expensediary.entity.Role;

import java.util.HashSet;
import java.util.Set;

public record UserInfo(
        String username,
        boolean userActiveStatus,
        Set<String> roles
) {}
