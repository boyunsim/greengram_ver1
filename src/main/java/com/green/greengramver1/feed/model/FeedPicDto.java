package com.green.greengramver1.feed.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "피드 사진 등록")
public class FeedPicDto {
    private long feedId;
    private String pic;
}
