package com.green.greengramver.config.security.oauth.userInfo;

import java.util.Map;
import java.util.Optional;

/*
    "response": {
        "id": id값

    }
*/
public class NaverOAuth2UserInfo extends Oauth2UserInfo {
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    private Optional<Map<String, Object>> getResponse() {
        return Optional.ofNullable((Map<String, Object>) attributes.get("response"));
    }

    @Override
    public String getId() {
        return getResponse().get().get("id").toString();
    }

    @Override
    public String getName() {
        return getResponse().get().get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return getResponse().get().get("email").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return getResponse().get().get("profile_image").toString();
    }
}
