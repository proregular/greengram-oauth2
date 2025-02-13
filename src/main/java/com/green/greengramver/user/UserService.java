package com.green.greengramver.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengramver.common.CommonUtils;
import com.green.greengramver.common.CookieUtils;
import com.green.greengramver.common.MyFileUtils;
import com.green.greengramver.common.exception.CustomException;
import com.green.greengramver.common.exception.UserErrorCode;
import com.green.greengramver.config.jwt.JwtProperties;
import com.green.greengramver.config.jwt.JwtUser;
import com.green.greengramver.config.jwt.TokenProvider;
import com.green.greengramver.config.security.AuthenticationFacade;
import com.green.greengramver.config.security.SignInProviderType;
import com.green.greengramver.entity.User;
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
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    public int signUp(MultipartFile pic, UserSignUpReq p) {
        String saveFileName = (pic != null) ? myFileUtils.makeRandomFileName(pic) : null; // 저장할 파일명

        String hashedPassword = BCrypt.hashpw(p.getUpw(), BCrypt.gensalt());

        User user = new User();
        user.setProviderType(SignInProviderType.LOCAL);
        user.setNickName(p.getNickName());
        user.setUid(p.getUid());
        user.setUpw(hashedPassword);
        user.setPic(saveFileName);

        // DB Insert
        //int result = mapper.insUser(p);
        userRepository.save(user);

        // 유저정보가 성공적으로 저장됐고 사진 파일이 존재할 경우
        if(pic != null) {
            //파일 업로드(저장할 경로 user/{유저번호})
            String middlePath = String.format("user/%d", user.getUserId());

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

        return 1;
    }

    public UserSignInRes signIn(UserSignInReq p, HttpServletResponse response) {
        User user = userRepository.findByUidAndProviderType(p.getUid(), SignInProviderType.LOCAL);
        //UserSignInRes res = mapper.selUserByUid(p.getUid());

        if(user == null || !passwordEncoder.matches(p.getUpw(), user.getUpw())) {
            throw new CustomException(UserErrorCode.INCORRECT_ID_PW);
        }

        /*
        JWT 토큰 생성 2개 AccessToken(20분), RefereshToken(15일)
        */
        JwtUser jwtUser = new JwtUser();

        jwtUser.setSignedUserId(user.getUserId());

        List<String> roles = new ArrayList<>(2);

        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        jwtUser.setRoles(roles);

        String accessToken = tokenProvider.generateToken(jwtUser, Duration.ofSeconds(30));
        String refreshToken = tokenProvider.generateToken(jwtUser, Duration.ofDays(15));

        
        int maxAge = 1_296_000; // 15 * 24 * 60 * 60 15일의 초(second)값

        cookieUtils.setCookie(response, "refreshToken", refreshToken, 1296000, "/api/user/access-token");

        return new UserSignInRes(user.getUserId()
                , user.getNickName()
                , user.getPic()
                , accessToken);
    }

    public UserInfoGetRes getUserInfo(UserInfoGetReq p) {
        p.setSignedUserId(authenticationFacade.getSignedUserId());
        return mapper.selUserInfo(p);
    }

    public  String getAccessToken(HttpServletRequest req) {
        Cookie cookie = cookieUtils.getCookie(req, "refreshToken");
        String refreshToken = cookie.getValue();
        log.info("refreshToken: {}", refreshToken);

        JwtUser jwtUser = tokenProvider.getUser(refreshToken);
        String accessToken = tokenProvider.generateToken(jwtUser, Duration.ofSeconds(30));

        return accessToken;
    }

    public String patchUserPic(UserPicPatchReq p) {
        p.setSignedUserId(authenticationFacade.getSignedUserId());
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
