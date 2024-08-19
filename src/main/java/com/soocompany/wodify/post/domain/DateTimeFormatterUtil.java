package com.soocompany.wodify.post.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterUtil {
    // 연, 월, 일만 출력하는 포맷
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // 연, 월, 일과 시간을 모두 출력하는 포맷
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 날짜만 포맷팅 (연, 월, 일)
    public static String dateOnly(LocalDateTime dateTime) {
        return dateTime.format(DATE_ONLY_FORMATTER);
    }

    // 날짜와 시간을 모두 포맷팅
    public static String dateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }
}
