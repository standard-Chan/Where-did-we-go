package com.wheredidwego.util.lib;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Date {
    static public String localDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return date.format(formatter);
    }

    static public LocalDate stringToLocalDate(String raw) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(raw, formatter);
    }
}
