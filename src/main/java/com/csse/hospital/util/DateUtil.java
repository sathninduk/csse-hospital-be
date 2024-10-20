package com.csse.hospital.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    public static Timestamp[] convertToTimestamps(String start, String end) {
        try {
            // Define the expected date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Convert start and end to LocalDate
            LocalDate startDate = LocalDate.parse(start, formatter);
            LocalDate endDate = LocalDate.parse(end, formatter);

            // Convert LocalDate to LocalDateTime
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

            // Convert LocalDateTime to Timestamp
            Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
            Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

            return new Timestamp[]{startTimestamp, endTimestamp};
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Timestamp format must be yyyy-MM-dd", e);
        }
    }
}