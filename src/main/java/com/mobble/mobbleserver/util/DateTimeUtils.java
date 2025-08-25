package com.mobble.mobbleserver.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public enum DateTimeUtils {
    ;

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static LocalDateTime now() {
        return ZonedDateTime.now(ZONE_ID)
                .toLocalDateTime();
    }

    public static LocalDateTime toKST(LocalDateTime dateTime) {
        return dateTime
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZONE_ID)
                .toLocalDateTime();
    }
}
