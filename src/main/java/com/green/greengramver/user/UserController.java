package com.green.greengramver.user;

import com.green.greengramver.common.model.ResultResponse;
import com.green.greengramver.user.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
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
    public ResultResponse<UserSignInRes> signIn(@RequestBody UserSignInReq p, HttpServletResponse response) {
        UserSignInRes res = service.signIn(p, response);

        return ResultResponse.<UserSignInRes>builder()
                .resultMessage("로그인 성공")
                .resultData(res)
                .build();
    }
    
    @GetMapping
    @Operation(summary = "유저 프로필 정보")
    public ResultResponse<UserInfoGetRes> getUserInfo(@ParameterObject @ModelAttribute UserInfoGetReq p) {
        log.info("UserController > getUserInfo {}", p);
        UserInfoGetRes res = service.getUserInfo(p);

        return ResultResponse.<UserInfoGetRes>builder()
                .resultMessage("유저 프로필 정보")
                .resultData(res)
                .build();
    }

    @GetMapping("access-token")
    @Operation(summary = "accessToken 재발행")
    public ResultResponse<String> getAccessToken(HttpServletRequest req) {
        String accessToken = service.getAccessToken(req);

        return ResultResponse.<String>builder()
                .resultMessage("유저 프로필 정보")
                .resultData(accessToken)
                .build();
    }

    @PatchMapping("/pic")
    public ResultResponse<String> patchProfilePic(@ModelAttribute UserPicPatchReq p) {
        log.info("UserController > PatchProfilePic > p {}", p);

        String pic = service.patchUserPic(p);

        return ResultResponse.<String>builder()
                .resultMessage("프로필 사진 수정 완료")
                .resultData(pic)
                .build();
    }

}
