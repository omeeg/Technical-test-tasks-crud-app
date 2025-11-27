package com.example.dts.validation;

import java.time.LocalDateTime;

public class ValidDateHelper {

    //Could change this / refactor this ot have more validation
    //e.g. if we didn't want the date to be created too soon.
    //Or if higher priority tasks need longer due dates. Can handle business logic
    //checks here
    public static boolean isDateInPast(LocalDateTime dateTime) {
        return dateTime.isBefore(LocalDateTime.now());
    }
}
