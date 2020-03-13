package it.step.msauth.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeMapper {
    public static LocalDateTime stringToLocalDateTime(String dateStr) {
        return LocalDateTime.parse(dateStr.substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
}
