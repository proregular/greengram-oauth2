package com.green.greengramver.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Schema(description = "피드 등록 정보")
@ToString
public class FeedPostReq {
    @JsonIgnore
    private long feedId;
    @Schema(description = "글쓴이 아이디", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private long writerUserId;
    @Schema(description = "피드 내용", example = "블라블라")
    private String contents;
    @Schema(description = "피드 위치", example = "대구")
    private String location;
}
