package com.example.dts.validation;

public class ValidTitleHelper {

    public static boolean isBlankTitle(String title) {
        return title==null || title.trim().isEmpty();
    }

    public static boolean isTitleTooLong(String title) {
        return title.length()>255;
    }

}
