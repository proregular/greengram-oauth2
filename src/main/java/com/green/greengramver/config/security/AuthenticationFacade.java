package com.green.greengramver.config.security;

import com.green.greengramver.config.jwt.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public JwtUser getSignedUser() {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return myUserDetails == null ? null : myUserDetails.getJwtUser();
    }

    public long getSignedUserId() {
        return getSignedUser().getSignedUserId();
    }
}
