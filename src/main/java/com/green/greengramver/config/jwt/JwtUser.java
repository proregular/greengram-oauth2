package com.green.greengramver.config.jwt;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class JwtUser {
    private long signedUserId;
    private List<String> roles; // 인가(권한)처리 때 사용, ROLE_이름, ROLE_USER, ROLE_ADMIN
}
