package com.green.greengramver.common;

public class CommonUtils {
    public static String getRandomNickName() {
        String[] strList1 = {"우주니", "송그나", "사공그", "보유니", "햐지"};
        String[] strList2 = {"의 사과나무", "의 길", "에게 주는 편지", "는 바보다", "에게 압도적 감사"};

        int ran1 = (int)(Math.random() * strList1.length);
        int ran2 = (int)(Math.random() * strList2.length);

        String ranNickName = strList1[ran1] + strList2[ran2];

        return ranNickName;
    }
}
