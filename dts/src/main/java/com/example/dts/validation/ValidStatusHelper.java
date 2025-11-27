package com.example.dts.validation;

import com.example.dts.data.entities.TaskStatus;

import java.util.Set;

public class ValidStatusHelper {

    private static final Set<String> VALID_STATUS = Set.of("TO_DO", "IN_PROGRESS", "COMPLETED");
    public static boolean isCompletedStatus(String status) {
        return status.equals("COMPLETED");
    }

    public static boolean isValidStatus(String status) {
        return VALID_STATUS.contains(status);
    }

}
