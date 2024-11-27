package com.green.greengramver1.user;

import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.user.model.UserSignInReq;
import com.green.greengramver1.user.model.UserSignInRes;
import com.green.greengramver1.user.model.UserSignUpReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final MyFileUtils myFileUtils;

    public int postSignUp(MultipartFile pic, UserSignUpReq p) {
        //프로필 이미지 파일 처리
        //String savedPicName = myFileUtils.makeRandomFileName(pic.getOriginalFilename());
        String savedPicName = pic != null ? myFileUtils.makeRandomFileName(pic) : null;

        String hashedPassword = BCrypt.hashpw(p.getUpw(), BCrypt.gensalt());
        log.info("hashedPassword: {}", hashedPassword);
        p.setUpw(hashedPassword);
        p.setPic(savedPicName);

        int result = mapper.insUser(p);
        //호출하고 나면 UserSignUpReq에 pk값이 생성된다.

        if(pic == null) {
            return result;
        }

        // user/${userId}(pk값)/${savedPicName}
        // middlePath = user/${userId}
        long userId = p.getUserId(); //userId를 insert 후에 얻을 수 있다.
        String middlePath = String.format("user/%d", userId);
        myFileUtils.makeFolders(middlePath);
        log.info("middlePath: {}", middlePath);
        String filePath = String.format("%s/%s", middlePath, savedPicName);

        try {
            myFileUtils.transferTo(pic, filePath); //pic 객체의 내용을 filepath경로로 이동
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public UserSignInRes postSignIn(UserSignInReq p) {
        UserSignInRes res = mapper.selUserForSignIn(p);
        if(res == null) {//uid가 같은 정보가 데이터베이스에 없다
            res = new UserSignInRes();
            res.setMessage("아이디를 확인해 주세요.");
            return res;
        }else if( !BCrypt.checkpw(p.getUpw(), res.getUpw())) {
            res = new UserSignInRes();
            res.setMessage("비밀번호가 틀렸습니다.");
            return res;
        }
        res.setMessage("로그인 성공");
        return res;
    }

}
