package com.green.greengramver1.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component //빈등록 (spring container한테 객체화 맡기는 것
           //, 주소값 들고 있어달라고 요청 필요할 때 singletone 방식으로 같은 주소값을 shallow copy로 줄 수 있도록 하는 것)
public class MyFileUtils {
    private final String uploadPath;

    /*
        @Value("${file.directory}")은
        yaml 파일에 있는 file.directory 속성에 저장된 값을 생성자 호출할 때 값을 넣어준다.
     */
    public MyFileUtils(@Value("${file.directory}") String uploadPath) {
        log.info("MyFileUtils - 생성자: {}", uploadPath);
        this.uploadPath = uploadPath;
    }

    // path ="ddd/aaa"
    // D:/Students/download/greengram_ver1/ddd/aaa
    //디렉토리 생성
    public String makeFolders(String path) {
        File file = new File(uploadPath, path);
        // static 아님 >> 객체화하고 주소값.(file.)으로 호출했기 때문에
        // 리턴타입은 boolean >> if()안에서 호출했기 때문에
        // 파라미터는 없음 >> 호출 때 인자를 보내지 않았기 때문에
        // 메소드명은 >> exists였다.
        if(!file.exists()){
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    //파일명에서 확장자 추출
    public String getExt(String fileName) {
        int lastIdx = fileName.lastIndexOf(".");
        return fileName.substring(lastIdx);
    }

    //랜덤파일명 생성
    public String makeRandomFileName() {
        return UUID.randomUUID().toString();
    }
    //?// 메소드는 한가지 일만 하도록 하기위해 나눈건가? 수정의 가능성이 있어서?
    //랜덤 파일명 + 확장자 생성
    public String makeRandomFileName(String originalFileName) {
        //return this.makeRandomFileName().concat(this.getExt(originalFileName));
        return makeRandomFileName() + getExt(originalFileName);
    }

    public String makeRandomFileName(MultipartFile file) {
        return makeRandomFileName(file.getOriginalFilename());
    }

    //파일을 원하는 경로에 저장
    public void transferTo(MultipartFile multipartFile, String path) throws IOException {
        File file = new File(uploadPath, path);
        multipartFile.transferTo(file);
    }

}