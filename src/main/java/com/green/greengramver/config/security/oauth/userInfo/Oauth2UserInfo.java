package com.green.greengramver.config.security.oauth.userInfo;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/*
플랫폼으로부터 받고 싶은 데이터 저장하고
원하는 데이터를 리턴하는 규격
*/
@RequiredArgsConstructor
public abstract class Oauth2UserInfo {
    protected  final Map<String, Object> attributes; // UserInfo Json > Map

    public abstract String getId(); // 유일값 리턴 용도로 쓸 메소드
    public abstract String getName(); // 유일값 리턴 용도로 쓸 메소드
    public abstract String getEmail(); // 유일값 리턴 용도로 쓸 메소드
    public abstract String getProfileImageUrl(); // 유일값 리턴 용도로 쓸 메소드

}
