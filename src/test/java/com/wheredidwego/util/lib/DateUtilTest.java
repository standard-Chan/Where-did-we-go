package com.wheredidwego.util.lib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void stringToLocalDate() {
        DateUtil dateUtil = new DateUtil();
        assertEquals(dateUtil.stringToLocalDate("2024-05-03"), "2024.05.03");

    }
}