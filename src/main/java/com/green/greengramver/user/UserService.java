package com.green.greengramver.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengramver.common.CommonUtils;
import com.green.greengramver.common.CookieUtils;
import com.green.greengramver.common.MyFileUtils;
import com.green.greengramver.config.jwt.JwtProperties;
import com.green.greengramver.config.jwt.JwtUser;
import com.green.greengramver.config.jwt.TokenProvider;
import com.green.greengramver.user.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final MyFileUtils myFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CookieUtils cookieUtils;

    public int signUp(MultipartFile pic, UserSignUpReq p) {
        String saveFileName = (pic != null) ? myFileUtils.makeRandomFileName(pic) : null; // 저장할 파일명

        // 파일명, 비밀번호(암호화된) Set
        p.setPic(saveFileName);
        p.setUpw(BCrypt.hashpw(p.getUpw(), BCrypt.gensalt()));


        if(p.getNickName() == null || p.getNickName() == "") {
            p.setNickName(CommonUtils.getRandomNickName());
        }

        // DB Insert
        int result = mapper.insUser(p);

        // 유저정보가 성공적으로 저장됐고 사진 파일이 존재할 경우
        if(result == 1 && pic != null) {
            //파일 업로드(저장할 경로 user/{유저번호})
            String middlePath = String.format("user/%d", p.getUserId());

            // 폴더 생성
            myFileUtils.makeFolders(middlePath);

            // 파일 저장 경로
            String path = middlePath + "/" + saveFileName;

            // 파일 저장
            try {
                myFileUtils.transferTo(pic, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public UserSignInRes signIn(UserSignInReq p, HttpServletResponse response) {
        UserSignInRes res = mapper.selUserByUid(p.getUid());

        if(res == null) {
            res = new UserSignInRes();
            res.setMessage("아이디를 확인해 주세요.");
        } else if(!passwordEncoder.matches(p.getUpw(), res.getUpw())) {
            res = new UserSignInRes();
            res.setMessage("비밀번호를 확인해 주세요.");
        } else {
            res.setMessage("로그인 성공");
        }

        /*
        JWT 토큰 생성 2개 AccessToken(20분), RefereshToken(15일)
        */
        JwtUser jwtUser = new JwtUser();

        jwtUser.setSignedUserId(res.getUserId());

        List<String> roles = new ArrayList<>(2);

        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        jwtUser.setRoles(roles);

        String accessToken = tokenProvider.generateToken(jwtUser, Duration.ofMinutes(20));
        String refreshToken = tokenProvider.generateToken(jwtUser, Duration.ofDays(15));

        res.setAccessToken(accessToken);
        int maxAge = 1_296_000; // 15 * 24 * 60 * 60 15일의 초(second)값

        cookieUtils.setCookie(response, "refreshToken", refreshToken, 1296000);

        return res;
    }

    public UserInfoGetRes getUserInfo(UserInfoGetReq p) {
        return mapper.selUserInfo(p);
    }

    public  String getAccessToken(HttpServletRequest req) {
        Cookie cookie = cookieUtils.getCookie(req, "refreshToken");
        String refreshToken = cookie.getValue();
        log.info("refreshToken: {}", refreshToken);

        return refreshToken;
    }

    public String patchUserPic(UserPicPatchReq p) {
        //1. 저장할 파일명 생성(Random) 확장자는 오리지널 파일명과 일치하게한다.
        String savedPicName = (p.getPic() != null ? myFileUtils.makeRandomFileName(p.getPic().getOriginalFilename()) : null);

        String folderPath = String.format("/user/%d", p.getSignedUserId());
        myFileUtils.makeFolders(folderPath);

        //2. 기존 파일 삭제(방법 2가지: [1]: 폴더를 지운다. [2]select해서 기존 파일명을 얻어온다. [3]기존 파일명을 FE에서 받는다.)
        String deletePath = String.format("%s/user/%d",myFileUtils.getUploadPath(), p.getSignedUserId());
        myFileUtils.deleteFolder(deletePath, false);

        //4. DB에 튜플을 수정(update)한다.
        p.setPicName(savedPicName);

        int result = mapper.updUserPic(p);

        if(p.getPic() == null) { return null; }

        //3. 원하는 위치에 저장할 파일명으로 파일을 이동(TransferTo)한다.
        String filePath = String.format("user/%d/%s", p.getSignedUserId(), savedPicName);

        try {
            myFileUtils.transferTo(p.getPic(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return savedPicName;
    }

}
