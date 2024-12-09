package com.green.greengramver.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyFileUtilsTest {
    private final String FILE_DIRECTORY = "D:/SGSG/download/green_gram_ver3";
    MyFileUtils myFileUtils;

    @BeforeEach
    void setUp() {
        myFileUtils = new MyFileUtils(FILE_DIRECTORY);
    }

    @Test
    void deleteFolder() {
        String middlePath = String.format("%s/user/ddd", FILE_DIRECTORY);
        myFileUtils.deleteFolder(middlePath, false);
    }

    @Test
    void deleteFolder2() {
    }

    @Test
    void deleteFolder3() {
    }
}