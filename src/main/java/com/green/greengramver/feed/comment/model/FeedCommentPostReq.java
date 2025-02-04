package com.green.greengramver.feed.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "피드 댓글 등록")
public class FeedCommentPostReq {
    @Schema(description = "피드 PK", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;

    @Schema(description = "댓글 내용", example = "댓글 테스트")
    private String comment;
}
