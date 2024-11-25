package com.green.greengramver1.feed;

import com.green.greengramver1.common.model.ResultResponse;
import com.green.greengramver1.feed.model.FeedGetReq;
import com.green.greengramver1.feed.model.FeedGetRes;
import com.green.greengramver1.feed.model.FeedPostReq;
import com.green.greengramver1.feed.model.FeedPostRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("feed")
@Tag(name = "2.피드", description = "피드 관리")
public class FeedController {
    private final FeedService service;

    @PostMapping
    public ResultResponse<FeedPostRes> postFeed(@RequestPart List<MultipartFile> pics
                                              , @RequestPart FeedPostReq p) {
        FeedPostRes res = service.insFeed(pics, p);
        return ResultResponse.<FeedPostRes>builder()
                .resultMessage("피드 등록 완료")
                .resultData(res)
                .build();
    }
    /*
        QueryString - URL에 KEY, VALUE값을 포함한다.
     */

    @GetMapping
    public ResultResponse<List<FeedGetRes>> getFeedList(@ParameterObject @ModelAttribute FeedGetReq p) {
        log.info("p: {}", p);
        List<FeedGetRes> list = service.getFeedList(p);
        return ResultResponse.<List<FeedGetRes>>builder()
                .resultMessage(String.format("%d rows", list.size()))
                .resultData(list)
                .build();
    }

    @GetMapping("/param")
    public ResultResponse<List<FeedGetRes>> getFeedList(@RequestParam int page
                                                      , @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        if(size <= 0) {
            size = 20;
        }
        FeedGetReq p = new FeedGetReq(page, size);
        List<FeedGetRes> list = service.getFeedList(p);
        return ResultResponse.<List<FeedGetRes>>builder()
                .resultMessage(String.format("%d rows", list.size()))
                .resultData(list)
                .build();
    }

//    @GetMapping
//    public ResultResponse<Integer> getFeedList(@ParameterObject @ModelAttribute FeedGetReq p) {
//        log.info("p: {}", p);
//        return ResultResponse.<Integer>builder()
//                .resultMessage("테스트")
//                .resultData(1)
//                .build();
//    }
}
