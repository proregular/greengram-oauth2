package com.green.greengramver.user.follow;

import com.green.greengramver.common.model.ResultResponse;
import com.green.greengramver.user.follow.model.UserFollowReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/follow")
public class UserFollowController {
    private final UserFollowService service;

    //팔로우 신청
    // reqeustBody, 요청을 보내는자가 body에 json형태의 데이터를 보낸다는 뜻
    @PostMapping
    public ResultResponse<Integer> postUserFollow(@RequestBody UserFollowReq p) {
        log.info("Post: UserFollowReq p: {}", p);

        int result = service.postUserFollow(p);

        return ResultResponse.<Integer>builder()
                .resultMessage("팔로우를 추가 했습니다.")
                .resultData(result)
                .build();
    }

    // 팔로우 취소
    // 요청을 보내는 자가 데이터를 어떻게 보내나? Query-String
    @DeleteMapping
    public ResultResponse<Integer> deleteUserFollow(@ParameterObject @ModelAttribute UserFollowReq p) {
        log.info("Delete: UserFollowReq p: {}", p);

        int result = service.deleteUserFollow(p);

        return ResultResponse.<Integer>builder()
                .resultMessage("팔로우를 취소 했습니다.")
                .resultData(result)
                .build();
    }
}
