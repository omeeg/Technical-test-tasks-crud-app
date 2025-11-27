package com.example.dts.validation;

import java.util.Set;

public class ValidOrderByHelper {


    //For now only allowed to sort by dueDate, creationDate
    private static final Set<String> allowedSortFields = Set.of("dueDate", "creationDate");
    public static boolean isValidOrderBy(String orderBy) {
        return allowedSortFields.contains(orderBy);
    }
}
