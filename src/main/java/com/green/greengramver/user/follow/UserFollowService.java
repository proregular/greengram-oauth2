package com.green.greengramver.user.follow;

import com.green.greengramver.config.security.AuthenticationFacade;
import com.green.greengramver.user.follow.model.UserFollowReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFollowService {
    private final UserFollowMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public int postUserFollow(UserFollowReq p) {
        p.setFromUserId(authenticationFacade.getSignedUserId());
        return mapper.insUserFollow(p);
    }

    public int deleteUserFollow(UserFollowReq p) {
        p.setFromUserId(authenticationFacade.getSignedUserId());
        return mapper.delUserFollow(p);
    }
}
