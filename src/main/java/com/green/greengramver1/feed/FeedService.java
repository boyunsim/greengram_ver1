package com.green.greengramver1.feed;

import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.feed.model.FeedPicDto;
import com.green.greengramver1.feed.model.FeedPostReq;
import com.green.greengramver1.feed.model.FeedPostRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedMapper mapper;
    private final MyFileUtils myFileUtils; //DI받는 것

    public FeedPostRes insFeed(List<MultipartFile> pics, FeedPostReq p) {
        int result = mapper.insFeed(p);
        //파일 저장
        //위치: feed/${feedId}
        String middlePath = String.format("feed/%d", p.getFeedId());

        //폴더 만들기
        myFileUtils.makeFolders(middlePath);
//        for (int i = 0; i < pics.size(); i++) {
//            String savedPicName = myFileUtils.makeRandomFileName(pics.get(i));
//            String filePath = String.format("%s/%s", middlePath, savedPicName);
//
//            try {
//                myFileUtils.transferTo(pics.get(i), filePath);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        //파일 저장

        FeedPicDto feedPicDto = new FeedPicDto();
        //feedPicDto에 feedId값 넣어주세요.
        feedPicDto.setFeedId(p.getFeedId());

        List<String> list = new ArrayList<>();
        for(MultipartFile pic: pics) {
            String savedPicName = myFileUtils.makeRandomFileName(pic);
            String filePath = String.format("%s/%s", middlePath, savedPicName);
            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            feedPicDto.setPic(savedPicName);
            list.add(savedPicName);
            mapper.insFeedPic(feedPicDto);
        }
        FeedPostRes res = new FeedPostRes();

        res.setPics(list);
        res.setFeedId(p.getFeedId());

        return res;
    }
}
