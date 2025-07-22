package com.mobble.mobbleserver.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public enum DateTimeUtils {
    ;

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static String now() {
        return ZonedDateTime.now(ZONE_ID)
                .format(FORMATTER);
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZONE_ID)
                .format(FORMATTER);
    }
}
