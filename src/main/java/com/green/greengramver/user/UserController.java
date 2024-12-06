package com.green.greengramver.user;

import com.green.greengramver.common.model.ResultResponse;
import com.green.greengramver.user.model.UserSignInReq;
import com.green.greengramver.user.model.UserSignInRes;
import com.green.greengramver.user.model.UserSignUpReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name="유저", description = "유저 관련 API")
public class UserController {
    private final UserService service;

    @PostMapping("sign-up")
    @Operation(summary = "회원 가입")
    public ResultResponse<Integer> signUp(@RequestPart(required = false) MultipartFile pic,@RequestPart UserSignUpReq p) {
        log.info("파일정보: {}", pic);

        int result = service.signUp(pic, p);

        return ResultResponse.<Integer>builder()
                .resultMessage("회원 가입 완료")
                .resultData(result)
                .build();
    }

    @PostMapping("sign-in")
    @Operation(summary = "로그인")
    public ResultResponse<UserSignInRes> signIn(@RequestBody UserSignInReq p) {
        UserSignInRes res = service.signIn(p);

        return ResultResponse.<UserSignInRes>builder()
                .resultMessage(res.getMessage())
                .resultData(res)
                .build();
    }

}
