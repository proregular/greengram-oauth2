package com.green.greengramver;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {
    // 파라미터 dateTime으로 넘어오는 값이 DB에 저장된 dateTime값
    public static void assertCurrentTimeStamp(String dateTime) {
        // 자바에서 현재 일시 데이터
        LocalDateTime expectedNow = LocalDateTime.now(); //2024-12-30 11:32:23
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime actualdNow = LocalDateTime.now();
        assertTrue(Duration.between(expectedNow, actualdNow).getSeconds() <= 1);
    }
}
