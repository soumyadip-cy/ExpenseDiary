package org.soumyadip.expensediary.dto;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

public record AccessTimes(
        @NotNull
        ArrayList<AccessTimeDTO> accessTimes
) {
}
