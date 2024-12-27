package com.green.greengramver.feed.like.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "유저 좋아요 정보")
public class FeedLikeReq {
    @Schema(description = "피드 아이디", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;
    @Schema(description = "유저 아이디", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
