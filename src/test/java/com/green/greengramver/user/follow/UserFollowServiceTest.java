package com.green.greengramver.user.follow;

import com.green.greengramver.config.security.AuthenticationFacade;
import com.green.greengramver.user.follow.model.UserFollowReq;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

// Spring Test Context (컨테이너) 이용하는 것이 아니다.
@ExtendWith(MockitoExtension.class)
class UserFollowServiceTest {
    @InjectMocks
    UserFollowService userFollowService; // Mockito가 객체화를 직접 한다.

    @Mock
    UserFollowMapper userFollowMapper;

    @Mock
    AuthenticationFacade authenticationFacade;

    static final long fromUserId1 = 1L;
    static final long toUserId2 = 3L;
    static final long fromUserId3 = 3L;
    static final long toUserId4 = 4L;

    static final UserFollowReq userFollowReq1_2 = new UserFollowReq(toUserId2);
    static final UserFollowReq userFollowReq3_4  = new UserFollowReq(toUserId4);

    @Test
    @DisplayName("postUserFollow 테스트")
    void postUserFollow() {
        //given
        //authenticationFacade Mock객체의 getSignedUserId()메소드를 호출하면 willReturn 값이 리턴이 되도록 세팅
        final int EXECTED_RESULT = 1;
        final long EXPECTED_FROM_USER_ID = fromUserId3;
        final long EXPECTED_TO_USER_ID = toUserId4;
        given(authenticationFacade.getSignedUserId()).willReturn(EXPECTED_FROM_USER_ID);

        UserFollowReq givenParam = new UserFollowReq(EXPECTED_TO_USER_ID);
        givenParam.setFromUserId(EXPECTED_FROM_USER_ID);
        given(userFollowMapper.insUserFollow(givenParam)).willReturn(EXECTED_RESULT);

        //when
        UserFollowReq actualParam = new UserFollowReq(EXPECTED_TO_USER_ID);
        int actualResult = userFollowService.postUserFollow(actualParam);

        //then
        assertEquals(EXECTED_RESULT, actualResult);
    }

    @Test
    @DisplayName("deleteUserFollow 테스트")
    void deleteUserFollow() {
        //given
        //authenticationFacade Mock객체의 getSignedUserId()메소드를 호출하면 willReturn 값이 리턴이 되도록 세팅
        final int EXECTED_RESULT = 14;
        final long FROM_USER_ID = fromUserId3;
        final long TO_USER_ID = toUserId4;
        given(authenticationFacade.getSignedUserId()).willReturn(FROM_USER_ID);

        UserFollowReq givenParam = new UserFollowReq(TO_USER_ID);
        givenParam.setFromUserId(FROM_USER_ID);

        given(userFollowMapper.insUserFollow(givenParam)).willReturn(EXECTED_RESULT);

        //when
        UserFollowReq actualParam = new UserFollowReq(TO_USER_ID);
        int actualResult = userFollowService.deleteUserFollow(actualParam);

        //then
        assertEquals(EXECTED_RESULT, actualResult);
    }

}