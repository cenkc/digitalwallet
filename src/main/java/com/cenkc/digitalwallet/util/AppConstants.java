package com.cenkc.digitalwallet.util;

public final class AppConstants {

    public static final String STANDART_EXCEPTION_RESPONSE_JSON =
            "{\n" +
                "\t\"time\": %s,\n" +
                "\t\"errorMessage\": \"%s\",\n" +
                "\t\"uri\": \"%s\",\n" +
                "\t\"method\": \"%s\"\n" +
            "}";

    // prevent instantiation
    private AppConstants() {}
}
