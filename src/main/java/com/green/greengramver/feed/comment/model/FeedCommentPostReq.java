package com.green.greengramver.feed.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "피드 댓글 등록")
public class FeedCommentPostReq {
    @JsonIgnore
    private long feedCommentId;

    @Schema(description = "피드 PK", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;
    @Schema(description = "로그인한 유저 PK", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonIgnore
    private long userId;
    @Schema(description = "댓글 내용", example = "댓글 테스트")
    private String comment;

    public void setSignedUserId(long signedUserId) {
        this.userId = signedUserId;
    }
}
