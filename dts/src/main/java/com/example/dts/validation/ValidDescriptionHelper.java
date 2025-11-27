package com.example.dts.validation;

public class ValidDescriptionHelper {

    public static boolean isDescriptionTooLong(String description) {
        return description != null && description.length() > 1000;
    }
}
