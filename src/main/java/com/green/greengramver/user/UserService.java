package com.green.greengramver.user;

import com.green.greengramver.common.CommonUtils;
import com.green.greengramver.common.MyFileUtils;
import com.green.greengramver.user.model.UserSignInReq;
import com.green.greengramver.user.model.UserSignInRes;
import com.green.greengramver.user.model.UserSignUpReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final MyFileUtils myFileUtils;

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

    public UserSignInRes signIn(UserSignInReq p) {
        UserSignInRes res = mapper.selUserByUid(p.getUid());

        if(res == null) {
            res = new UserSignInRes();
            res.setMessage("아이디를 확인해 주세요.");
        } else if(!BCrypt.checkpw(p.getUpw(),res.getUpw())) {
            res = new UserSignInRes();
            res.setMessage("비밀번호를 확인해 주세요.");
        } else {
            res.setMessage("로그인 성공");
        }

        return res;
    }

}
