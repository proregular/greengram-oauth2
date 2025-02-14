package com.green.greengramver.config.security.oauth;

import com.green.greengramver.config.jwt.JwtUser;
import lombok.Getter;

import java.util.List;

@Getter
public class Oauth2JwtUser extends JwtUser {
    private final String nickName;
    private final String pic;

    public Oauth2JwtUser(String nickName, String pic, long signedUserId, List<String> roles) {
        super(signedUserId, roles);
        this.nickName = nickName;
        this.pic = pic;
    }
}
