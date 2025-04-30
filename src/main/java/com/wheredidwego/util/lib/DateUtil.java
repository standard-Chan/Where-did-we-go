package com.wheredidwego.util.lib;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    /**
     *    LocalDate 객체를 dash 문자열로 변환 (ex  2024-05-06)
     */
    static public String localDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    /**
     *  String 날짜를 LocalDate로 변환
     * @param raw 날짜 문자열 (ex. "20240506" or "2024-05-06")
     * @return LocalDate
     */
    static public LocalDate stringToLocalDate(String raw) {
        // 2024-05-02 형태
        if (raw.contains("-")){
            return LocalDate.parse(raw);
        }
        // -가 없는 형태
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(raw, formatter);
    }

    /**
     * String날짜를 dash 형태로 변환 : 20240506 -> 2024-05-06
     */
    static public String formatToDashedDate(String compactDate) {
        if (compactDate.length() != 8) {
            throw new IllegalArgumentException("날짜형식이 yyyyMMdd 형식에 맞지 않습니다. : " + compactDate);
        }
        LocalDate date = LocalDate.parse(compactDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
